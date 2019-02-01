package com.zyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ProxyadminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyadminApplication.class, args);
    }

}

