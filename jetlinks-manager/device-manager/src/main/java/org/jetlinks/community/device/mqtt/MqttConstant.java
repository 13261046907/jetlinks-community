package org.jetlinks.community.device.mqtt;

public class MqttConstant {

    public static final String MQTT_USERNAME = "zhihui";

    public static final String MQTT_PASSWORD = "X08nzIXE/NIFHx3gLusk8mNRehWgTGIcWU8J9eP3";


        public static void main(String[] args) {
            String hexData = "307830332030783033203078303120307846342030783030203078303220307838352030784537"; // 十六进制数据
            int temperature = Integer.parseInt(hexData.substring(2, 4), 16); // 获取温度数据
//            int humidity = Integer.parseInt(hexData.substring(4), 16); // 获取湿度数据

            System.out.println("Temperature: " + temperature + "°C");
//            System.out.println("Humidity: " + humidity + "%");
        }
}
