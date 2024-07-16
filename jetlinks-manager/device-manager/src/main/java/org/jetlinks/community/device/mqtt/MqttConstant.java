package org.jetlinks.community.device.mqtt;

public class MqttConstant {

    public static final String MQTT_USERNAME = "zhihui";

    public static final String MQTT_PASSWORD = "X08nzIXE/NIFHx3gLusk8mNRehWgTGIcWU8J9eP3";


        public static void main(String[] args) {
            String hexData = "5455 686e 6430 3135 5158 646c 5245 4636 5355 5243 4e45 3145 5257 644e 5347 6848 546b 4e42 6432 5645 5158 644a 5245 4930 5455 524a 5a30 3149 5a7a 524f 5530 4633 5a55 5656 4d77 3d3d"; // 十六进制数据
            int temperature = Integer.parseInt(hexData.substring(2, 4), 16); // 获取温度数据
//            int humidity = Integer.parseInt(hexData.substring(4), 16); // 获取湿度数据

            System.out.println("Temperature: " + temperature + "°C");
//            System.out.println("Humidity: " + humidity + "%");
        }


}
