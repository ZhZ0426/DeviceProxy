package com.zyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author: zyl
 * @Description Admin启动类
 * @Date 11:48 2019/2/1
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ProxyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyAdminApplication.class, args);
    }

}

