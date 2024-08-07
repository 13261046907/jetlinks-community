package org.jetlinks.community.standalone.task;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetlinks.community.device.configuration.RedisUtil;
import org.jetlinks.community.device.mqtt.CRC16Utils;
import org.jetlinks.community.device.mqtt.MQTTConnect;
import org.jetlinks.community.standalone.goview.v2.model.DeviceInstancesTemplate;
import org.jetlinks.community.standalone.goview.v2.service.DeviceInstancesTemplateService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@EnableScheduling
public class TaskJob implements Job {
    /**
     * 手动获取Bean
     * @param context
     */
    @Override
    public void execute(JobExecutionContext context) {
        String id = context.getJobDetail().getKey().getName();
        System.out.println("Task executed: " + id);
        //获取Service类
        MQTTConnect mqttConnect = GetBeanUtils.getBean(MQTTConnect.class);
        DeviceInstancesTemplateService deviceInstancesTemplateService = GetBeanUtils.getBean(DeviceInstancesTemplateService.class);
        RedisUtil redisUtil = GetBeanUtils.getBean(RedisUtil.class);
        try {
            DeviceInstancesTemplate deviceInstancesTemplate = deviceInstancesTemplateService.getById(id);
            if(!Objects.isNull(deviceInstancesTemplate)){
                String instruction = deviceInstancesTemplate.getInstruction();
                String topic = deviceInstancesTemplate.getSendTopic();
                String crcResult = CRC16Utils.getCrcResult(instruction);
                log.info("crcResult:{}", JSONObject.toJSON(crcResult));
                byte[] payload = hexStringToByteArra(crcResult);
                String redisKey = "mqtt:"+ deviceInstancesTemplate.getDeviceId();
                MqttMessage message = new MqttMessage(payload);
                message.setId((int) (System.currentTimeMillis() / 1000));
                redisUtil.set(redisKey,instruction);
                log.info("invokedFunction-topic:{},message:{}",topic,crcResult);
                mqttConnect.pub(topic, message);
            }
        } catch (MqttException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private byte[] hexStringToByteArra(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                + Character.digit(hexString.charAt(i+1), 16));
        }
        return byteArray;
    }

}