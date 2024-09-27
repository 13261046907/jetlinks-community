package org.jetlinks.community.standalone;

import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.authorization.events.AuthorizingHandleBeforeEvent;
import org.hswebframework.web.crud.annotation.EnableEasyormRepository;
import org.hswebframework.web.logging.aop.EnableAccessLogger;
import org.hswebframework.web.logging.events.AccessLoggerAfterEvent;
import org.jetlinks.community.device.tcp.TcpClient;
import org.jetlinks.community.device.tcp.TcpServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@SpringBootApplication(scanBasePackages = "org.jetlinks.community", exclude = {
    ElasticsearchRestClientAutoConfiguration.class
})
@EnableCaching
@EnableEasyormRepository("org.jetlinks.community.**.entity")
//@EnableAopAuthorize
@EnableAccessLogger
@Slf4j
@MapperScan(basePackages = "org.jetlinks.community.standalone.goview.v2.mapper")
//@ComponentScan(basePackages = {"org.jetlinks.community.standalone.goview.v2.mapper",
//                                "org.jetlinks.community.timeseries",
//                                "org.jetlinks.community.notify.manager.service",
//                                "org.jetlinks.community.config",
//                                "org.jetlinks.community.io.file"})
public class JetLinksApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetLinksApplication.class, args);
        log.info("TcpServer...start");
        TcpServer.run();
        log.info("TcpServer...end");
    }

    @Component
    @Slf4j
    public static class AdminAllAccess {

        @EventListener
        public void handleAuthEvent(AuthorizingHandleBeforeEvent e) {
            //admin用户拥有所有权限
            if (e.getContext().getAuthentication().getUser().getUsername().equals("")) {
                e.setAllow(true);
            }
        }

        @EventListener
        public void handleAccessLogger(AccessLoggerAfterEvent event) {

            log.info("{}=>{} {}-{}", event.getLogger().getIp(), event.getLogger().getUrl(), event.getLogger().getDescribe(), event.getLogger().getAction());

        }
    }

}
