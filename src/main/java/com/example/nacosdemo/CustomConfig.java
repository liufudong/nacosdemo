package com.example.nacosdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Configuration// 注解使得配置更新后，Bean 也会重新加载
public class CustomConfig {

    @Value("${spring.datasource.url}")
    private String message;
    @Value("${string.text}")
    private String text;
    @Value("${string.age}")
    private int age;
    @Value("${string.address}")
    private String address;
    public String getMessage() {
        return message;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getText() {
        return text;
    }
}
