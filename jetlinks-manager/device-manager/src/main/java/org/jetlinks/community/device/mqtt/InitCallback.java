package org.jetlinks.community.device.mqtt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetlinks.community.device.configuration.RedisUtil;
import org.jetlinks.community.device.response.DeviceDetail;
import org.jetlinks.community.device.service.LocalDeviceInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * MQTT回调函数
 */
@Slf4j
@Component
public class InitCallback implements MqttCallback {
    @Getter
    private final LocalDeviceInstanceService service;

    @Getter
    private final RedisUtil redisUtil;

    public InitCallback(LocalDeviceInstanceService service,RedisUtil redisUtil) {
        this.service = service;
        this.redisUtil = redisUtil;
    }

    /**
   * MQTT 断开连接会执行此方法
   */
  @Override
  public void connectionLost(Throwable cause) {

  }

  /**
   * publish发布成功后会执行到这里
   */
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    log.info("消息发送成功");
  }

  /**
   * subscribe订阅后得到的消息会执行到这里
   * 此处是接受 publish发送的消息
   */
  @Override
  public void messageArrived(String topic, MqttMessage message) {
      String convertedHexString = byteArrayToHexString(message.getPayload());
      log.info("TOPIC: [{}] 消息: {}，id:{}", topic, convertedHexString,message.getId());
      String[] parts = topic.split("/");
      String topicId = parts[1];  // 设备id
      String redisKey = "mqtt:"+topicId;
      String currentMessage = redisUtil.get(redisKey)+"";
      log.info("currentMessage:{}",currentMessage);
      if(StringUtils.isNotBlank(currentMessage)){
          String startFunctionStr = currentMessage.substring(8, 12);
          //取整获取字符串长度
          Integer startFunction = Integer.valueOf(startFunctionStr);
          log.info("currentMessage:{},startFunction:{}",currentMessage,startFunction);
          String deviceId= currentMessage.substring(0, 2);
          Mono<DeviceDetail> deviceDetail = service.getDeviceDetail(deviceId);
          DeviceDetail block = deviceDetail.block();
          log.info("DeviceDetail:{}",JSONObject.toJSONString(block));
          String productMetadata = block.getProductMetadata();
          List<ProductProperties> propertiesList = new ArrayList<>();
          if(StringUtils.isNotBlank(productMetadata)){
              JSONObject productMetadataJson = JSONObject.parseObject(productMetadata);
              propertiesList = JSONArray.parseArray(productMetadataJson.getString("properties"), ProductProperties.class);
          }
          List<String> hexList = getHexList(convertedHexString, startFunction);
          log.info("hexList:{}",JSONObject.toJSONString(hexList));
          if(!CollectionUtils.isEmpty(hexList) && !CollectionUtils.isEmpty(propertiesList)){
              for (int i = 0; i <= propertiesList.size(); i++) { // Adjust t
                  Map<String, Object> propertiesMap = new HashMap<>();
                  ProductProperties productProperties = propertiesList.get(i);
                  log.info("name:{},value:{}",productProperties.getName(),productProperties.getId());
                  propertiesMap.put(productProperties.getId(),hexList.get(i));
                  syncSendMessageToDevice(deviceId,JSONObject.toJSONString(propertiesMap));
              }
          }
          /*if(startFunction == 8){
              String humidity = convertedHexString.substring(6, 10);
              String temperature = convertedHexString.substring(10, 14);
              String noise = convertedHexString.substring(14, 18);
              String co2= convertedHexString.substring(18, 22);
              String temperatureStr = hexToStr(temperature);
              String humidityStr = hexToStr(humidity);
              String noiseStr = hexToStr(noise);
              String co2Str = hexToStr(co2);
              log.info("湿度:{},温度:{},噪声值:{},二氧化碳值:{}",humidityStr,temperatureStr,noiseStr,co2Str);
              Map<String, Object> temperatureProperties = new HashMap<>();
              temperatureProperties.put("temperature",temperatureStr);
              syncSendMessageToDevice(deviceId,JSONObject.toJSONString(temperatureProperties));
              Map<String, Object> humidityProperties = new HashMap<>();
              humidityProperties.put("humidity",humidityStr);
              syncSendMessageToDevice(deviceId,JSONObject.toJSONString(humidityProperties));
              Map<String, Object> noiseProperties = new HashMap<>();
              noiseProperties.put("noise",noiseStr);
              syncSendMessageToDevice(deviceId,JSONObject.toJSONString(noiseProperties));
              Map<String, Object> co2Properties = new HashMap<>();
              co2Properties.put("co2",co2Str);
              syncSendMessageToDevice(deviceId,JSONObject.toJSONString(co2Properties));
          }*/
      }
  }

  private void syncSendMessageToDevice(String deviceId,String message){
      HttpUtils.sendPost("http://127.0.0.1:8848/device/instance/"+deviceId+"/property",message);
  }

  private String hexToStr(String hexValue){
      int decValue = Integer.parseInt(hexValue, 16);
      double dividedByTen = (double) decValue / 10.0;
      DecimalFormat df = new DecimalFormat("0.00");
      String result = df.format(dividedByTen);
      return result;
  }


  public static void main1(String[] args) {
      String url = "/101010/properties/report";
      String[] parts = url.split("/");
      String value = parts[1];  // 结果为 "10"
      System.out.println(value);

    String hexString = "030301F4000285E7";
    byte[] byteArray = hexStringToByteArray(hexString);
    System.out.println("Byte Array: " + Arrays.toString(byteArray));

    String convertedHexString = byteArrayToHexString(byteArray);
    System.out.println("Converted Hex String: " + convertedHexString);

    byte[] payload = hexStringToByteArray("030301F4000285E7");
    MqttMessage message = new MqttMessage(payload);
    System.out.println(JSONObject.toJSON(message));
    JSONObject json = new JSONObject();
    json.put("retained", message.isRetained());
    json.put("qos", message.getQos());

    StringBuilder payloadBuilder = new StringBuilder();
    for (byte b : message.getPayload()) {
        payloadBuilder.append(String.format("%02X", b));
    }
    json.put("payload", payloadBuilder.toString());
    json.put("duplicate", message.isDuplicate());
    json.put("id", message.getId());

    MqttMessage mqttMessage = JSONObject.parseObject(json.toString(), MqttMessage.class);
    System.out.println(json.toJSONString());
 }

    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                + Character.digit(hexString.charAt(i+1), 16));
        }
        return byteArray;
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder(2 * byteArray.length);
        for (byte b : byteArray) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

    public List<String> getHexList(String convertedHexString, int num){
        int startIndex = 6; // Starting index for the first humidity
        List<String> hexList = new ArrayList<>();
        int length = 4; // Length of each humidity substring
        for (int i = 0; i <= num; i++) { // Adjust the loop count based on how many substrings you want
            String hex= convertedHexString.substring(startIndex + (i * length), startIndex + ((i + 1) * length));
            String hexStr = hexToStr(hex);
            hexList.add(hexStr);
        }
        return  hexList;
    }

    public Boolean messageDate(long timestamp) {
        // 转换为 Instant
        Instant instant = Instant.ofEpochSecond(timestamp);
        // 将 Instant 转换为本地日期（根据系统时区）
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // 输出转换后的日期
        System.out.println("转换后的日期: " + date);

        // 获取今天的日期
        LocalDate today = LocalDate.now();

        // 判断是否为今天
        if (date.isEqual(today)) {
            return true;
        } else {
           return false;
        }
    }

}

