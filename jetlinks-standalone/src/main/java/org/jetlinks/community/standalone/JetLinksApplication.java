package org.jetlinks.community.standalone;

import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.authorization.events.AuthorizingHandleBeforeEvent;
import org.hswebframework.web.crud.annotation.EnableEasyormRepository;
import org.hswebframework.web.logging.aop.EnableAccessLogger;
import org.hswebframework.web.logging.events.AccessLoggerAfterEvent;
import org.jetlinks.community.device.tcp.NettyTcpServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


@SpringBootApplication(scanBasePackages = "org.jetlinks.community", exclude = {
    ElasticsearchRestClientAutoConfiguration.class
})
@EnableCaching
@EnableEasyormRepository("org.jetlinks.community.**.entity")
@EnableAccessLogger
@Slf4j
@MapperScan(basePackages = "org.jetlinks.community.standalone.goview.v2.mapper")
public class JetLinksApplication implements CommandLineRunner {


    @Autowired
    NettyTcpServer nettyTcpServer;

    public static void main(String[] args) {
        SpringApplication.run(JetLinksApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        //启动TCP服务
        InetSocketAddress tcpAddress = new InetSocketAddress("0.0.0.0", 9999);
        nettyTcpServer.start(tcpAddress);
    }

}
