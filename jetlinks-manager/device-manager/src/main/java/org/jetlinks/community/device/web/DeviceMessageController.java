package org.jetlinks.community.device.web;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.exception.BusinessException;
import org.hswebframework.web.id.IDGenerator;
import org.jetlinks.community.device.configuration.RedisUtil;
import org.jetlinks.community.device.entity.DeviceInstanceTemplateEntity;
import org.jetlinks.community.device.entity.DevicePropertiesEntity;
import org.jetlinks.community.device.mqtt.MQTTConnect;
import org.jetlinks.community.device.service.DeviceInstanceTemplateService;
import org.jetlinks.community.utils.ErrorUtils;
import org.jetlinks.core.device.DeviceOperator;
import org.jetlinks.core.device.DeviceRegistry;
import org.jetlinks.core.enums.ErrorCode;
import org.jetlinks.core.exception.DeviceOperationException;
import org.jetlinks.core.message.DeviceMessageReply;
import org.jetlinks.core.message.ReadPropertyMessageSender;
import org.jetlinks.core.message.WritePropertyMessageSender;
import org.jetlinks.core.message.property.ReadPropertyMessageReply;
import org.jetlinks.core.message.property.WritePropertyMessageReply;
import org.jetlinks.core.metadata.PropertyMetadata;
import org.jetlinks.core.metadata.types.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@RestController
@RequestMapping("/device")
@Slf4j
@Resource(id = "device-instance", name = "设备实例")
@Tag(name = "设备指令API")
@Deprecated
public class DeviceMessageController {

    @Autowired
    private DeviceRegistry registry;
    @Autowired
    private DeviceInstanceTemplateService deviceInstanceTemplateService;
    private final MQTTConnect mqttConnect;
    @Autowired
    private RedisUtil redisUtil;

    public DeviceMessageController(MQTTConnect mqttConnect) {
        this.mqttConnect = mqttConnect;
    }

    //设备功能调用
    @GetMapping("invoked/{deviceId}/function")
    @SneakyThrows
    @Deprecated
    public Flux<?> invokedDeviceMessage(@PathVariable String deviceId,@RequestParam String sendTopic,
                                        @RequestParam String acceptTopic,
                                   @RequestParam String instruction) {
        try {
//            String crcResult = CRC16Utils.getCrcResult(instruction);
            /**
             * 1、根据instruction指令查询设备模版表唯一一条数据
             * 2、判断设备类型，属性key:deviceId,开关key:instruction
             */
            Mono<DeviceInstanceTemplateEntity> byInstruction = deviceInstanceTemplateService.findByInstruction(sendTopic,instruction);
            byInstruction.subscribe(deviceInstanceTemplateEntity -> {
                // 异步处理每个结果
                if(!Objects.isNull(deviceInstanceTemplateEntity)) {
                    Integer deviceType = deviceInstanceTemplateEntity.getDeviceType();
                    if (!Objects.isNull(deviceType)) {
                        String redisKey = "";
                        if (1 == deviceType) {
                            redisKey = "mqtt:"+deviceId;
                        }else {
                            redisKey = "mqtt:"+instruction;
                            String templateEntityId = deviceInstanceTemplateEntity.getId();
                            //开关类型，判断开关
                            if(deviceInstanceTemplateEntity.getOpenInstructionCrc().equals(instruction)){
                                //开指令
                                log.info("开:templateEntityId:{}",templateEntityId);
                                deviceInstanceTemplateService.updateStatusById("1",templateEntityId);
                            }else if(deviceInstanceTemplateEntity.getCloseInstructionCrc().equals(instruction)){
                                log.info("关:templateEntityId:{}",templateEntityId);
                                //关指令
                                deviceInstanceTemplateService.updateStatusById("0",templateEntityId);
                            }

                        }
                        log.info("crcResult:{}",JSONObject.toJSON(instruction));
                        byte[] payload = hexStringToByteArray(instruction);
                        MqttMessage message = new MqttMessage(payload);
                        message.setId((int) (System.currentTimeMillis() / 1000));
                        redisUtil.set(redisKey,instruction);
                        log.info("invokedFunction-topic:{},message:{}",sendTopic,instruction);
                        try {
                            mqttConnect.pub(sendTopic, message);
                            mqttConnect.sub(acceptTopic);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        Thread.sleep(1500);
        return Flux.empty();
    }

    //获取设备属性
    @GetMapping("/{deviceId}/property/{property:.+}")
    @SneakyThrows
    @Deprecated
    public Flux<?> getProperty(@PathVariable String deviceId, @PathVariable String property) {

        return registry
            .getDevice(deviceId)
            .switchIfEmpty(ErrorUtils.notFound("设备不存在"))
            .map(DeviceOperator::messageSender)//发送消息到设备
            .map(sender -> sender.readProperty(property).messageId(IDGenerator.SNOW_FLAKE_STRING.generate()))
            .flatMapMany(ReadPropertyMessageSender::send)
            .map(mapReply(ReadPropertyMessageReply::getProperties));

    }

    //获取标准设备属性
    @GetMapping("/standard/{deviceId}/property/{property:.+}")
    @SneakyThrows
    @Deprecated
    public Mono<DevicePropertiesEntity> getStandardProperty(@PathVariable String deviceId, @PathVariable String property) {
        return Mono.from(registry
            .getDevice(deviceId)
            .switchIfEmpty(ErrorUtils.notFound("设备不存在"))
            .flatMapMany(deviceOperator -> deviceOperator.messageSender()
                .readProperty(property).messageId(IDGenerator.SNOW_FLAKE_STRING.generate())
                .send()
                .map(mapReply(ReadPropertyMessageReply::getProperties))
                .flatMap(map -> {
                    Object value = map.get(property);
                    return deviceOperator.getMetadata()
                        .map(deviceMetadata -> deviceMetadata.getProperty(property)
                            .map(PropertyMetadata::getValueType)
                            .orElse(new StringType()))
                        .map(dataType -> DevicePropertiesEntity.builder()
                            .deviceId(deviceId)
                            .productId(property)
                            .build()
                            .withValue(dataType, value));
                })))
            ;

    }

    //设置设备属性
    @PostMapping("/setting/{deviceId}/property")
    @SneakyThrows
    @Deprecated
    public Flux<?> settingProperties(@PathVariable String deviceId, @RequestBody Map<String, Object> properties) {

        return registry
            .getDevice(deviceId)
            .switchIfEmpty(ErrorUtils.notFound("设备不存在"))
            .map(operator -> operator
                .messageSender()
                .writeProperty()
                .messageId(IDGenerator.SNOW_FLAKE_STRING.generate())
                .write(properties)
            )
            .flatMapMany(WritePropertyMessageSender::send)
            .map(mapReply(WritePropertyMessageReply::getProperties));
    }

    //设备功能调用
    @PostMapping("invoked/{deviceId}/function/{functionId}")
    @SneakyThrows
    @Deprecated
    public Flux<?> invokedFunction(@PathVariable String deviceId,
                                   @PathVariable String functionId,
                                   @RequestBody Map<String, Object> properties) {

        try {
//            mqttConnect.setMqttClient(MqttConstant.MQTT_USERNAME, MqttConstant.MQTT_PASSWORD, initCallback);
            String openValue = properties.get("open")+"";
//            String ascii = StringToAscii(openValue);
           /* byte[] originalBytes = openValue.getBytes(StandardCharsets.UTF_8);
            // 转换为十六进制字符串
            String hexString = HexConverter.bytesToHex(originalBytes);*/
            byte[] payload = hexStringToByteArray(openValue);
            MqttMessage message = new MqttMessage(payload);
            log.info("message:{}",JSONObject.toJSON(message));
            String topic = "/" + deviceId + "/function/invoke";
            log.info("invokedFunction-topic:{},message:{}",topic,message);
            mqttConnect.pub(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return registry
            .getDevice(deviceId).flux();

    }

    //获取设备所有属性
    @PostMapping("/{deviceId}/properties")
    @SneakyThrows
    @Deprecated
    public Flux<?> getProperties(@PathVariable String deviceId,
                                 @RequestBody Mono<List<String>> properties) {

        return registry.getDevice(deviceId)
            .switchIfEmpty(ErrorUtils.notFound("设备不存在"))
            .map(DeviceOperator::messageSender)
            .flatMapMany((sender) ->
                properties.flatMapMany(list ->
                    sender.readProperty(list.toArray(new String[0]))
                        .messageId(IDGenerator.SNOW_FLAKE_STRING.generate())
                        .send()))
            .map(mapReply(ReadPropertyMessageReply::getProperties));
    }

    private static <R extends DeviceMessageReply, T> Function<R, T> mapReply(Function<R, T> function) {
        return reply -> {
            if (ErrorCode.REQUEST_HANDLING.name().equals(reply.getCode())) {
                throw new DeviceOperationException(ErrorCode.REQUEST_HANDLING, reply.getMessage());
            }
            if (!reply.isSuccess()) {
                throw new BusinessException(reply.getMessage(), reply.getCode());
            }
            T mapped = function.apply(reply);
            if (mapped == null) {
                throw new BusinessException(reply.getMessage(), reply.getCode());
            }
            return mapped;
        };
    }


    public byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                + Character.digit(hexString.charAt(i+1), 16));
        }
        return byteArray;
    }

    public String StringToAscii(String hex) {
        String result = "";
        for (int i = 0; i < hex.length(); i += 2) {
            String hexByte = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(hexByte, 16);
            if(StringUtils.isNotBlank(result)){
                result = result+" "+decimal;
            }else {
                result = decimal+"";
            }
            System.out.println("Hexadecimal: " + hexByte + "  Decimal: " + decimal);
        }
        System.out.println("十进制:"+result);
        String[] parts = result.split(" ");
        int[] intArray = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            intArray[i] = Integer.parseInt(parts[i]);
        }
        String ascii = "";
        for (int num : intArray) {
            char asciiChar = (char) num;
            ascii = ascii + asciiChar;
        }
       return ascii;
    }
}
