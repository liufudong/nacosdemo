package com.example.nacosdemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConfigController {

    private final CustomConfig customConfig;

    @Autowired
    public ConfigController(CustomConfig customConfig) {
        this.customConfig = customConfig;
    }

    @GetMapping("/message")
    public String getMessage() {
        return customConfig.getMessage()+"/t"+customConfig.getAddress()+customConfig.getText()+customConfig.getAge();
    }
}
