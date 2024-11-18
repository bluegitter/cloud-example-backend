package com.tellhow.example.test;

import com.tellhow.cloud.common.feign.annotation.EnableThFeignClients;
import com.tellhow.cloud.common.security.annotation.EnableThResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

/**
 * @author cloud archetype
 * <p>
 * 项目启动类
 */
@EnableThFeignClients
@EnableThResourceServer
@SpringBootApplication(exclude = {LiquibaseAutoConfiguration.class})
public class App {
    public static void main(String[] args) {
        System.setProperty("spring.main.allow-circular-references", "true");
        SpringApplication.run(App.class, args);
    }
}