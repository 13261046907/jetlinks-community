package org.jetlinks.community.device.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
@Configuration
public class DbUtils {
    private static final String URL = "jdbc:mysql://101.201.119.26:30016/jetlinks?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2b8";
    private static final String USER = "root";
    private static final String PASSWORD = "3+PloXbwcM7+JA=";

    private static HikariDataSource dataSource;

    static {
        // 设置 HikariCP 数据源配置
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        dataSource = new HikariDataSource(config);
    }

    public static void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        dataSource = new HikariDataSource(config);
    }


    public static void execute(String sql) {
        QueryRunner queryRunner = new QueryRunner(dataSource); // 直接传入 DataSource
        try (Connection connection = queryRunner.getDataSource().getConnection()) {
            // 执行数据库操作
            queryRunner.execute(sql);
        } catch (SQLException e) {
            // 重新连接数据库
            setupDataSource(); // 重新创建 HikariDataSource
            // 重新尝试获取连接
            try{
                queryRunner.execute(sql);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                dataSource.close(); // 关闭数据源，通常在应用程序关闭时调用
            }
        }
    }
}
