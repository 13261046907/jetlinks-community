package org.jetlinks.community.device.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * MQTT回调函数
 */
@Slf4j
@Component
public class InitCallback implements MqttCallback {
  /**
   * MQTT 断开连接会执行此方法
   */
  @Override
  public void connectionLost(Throwable cause) {
      try {
          MQTTConnect mqttConnect = new MQTTConnect();
          mqttConnect.setMqttClient(null,null,null);
          log.info("MQTT重新连接成功！");
      } catch (MqttException e) {
          log.error("MQTT启动连接失败！");
          log.error(e.getMessage(), e);
      }
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
      log.info("TOPIC: [{}] 消息: {}", topic, new String(message.getPayload(), Charset.forName("GBK")));
  }
}
