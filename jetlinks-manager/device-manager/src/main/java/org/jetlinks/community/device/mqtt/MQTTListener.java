package org.jetlinks.community.device.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动 监听主题
 */
@Slf4j
@Component
public class MQTTListener implements ApplicationRunner {
    private final MQTTConnect server;
    private final InitCallback initCallback;

  @Autowired
  public MQTTListener(MQTTConnect server, InitCallback initCallback) {
    this.server = server;
    this.initCallback = initCallback;
  }

  @Override
  public void run(ApplicationArguments args) {
      try {
          server.setMqttClient(MqttConstant.MQTT_USERNAME, MqttConstant.MQTT_PASSWORD, initCallback);
          server.sub("/10/properties/report");
          log.info("MQTT启动连接成功！");
      } catch (MqttException e) {
          log.error("MQTT启动连接失败！");
          log.error(e.getMessage(), e);
      }
  }
}


