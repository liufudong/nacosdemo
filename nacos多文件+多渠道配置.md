# 1.nacos多文件配置

```yml
spring:
  cloud:
    nacos:
      config:
        enabled: true  # 启用Nacos配置服务
        server-addr: 127.0.0.1:8848  # Nacos服务器的地址及端口
        file-extension: yml  # 配置文件的扩展名为YAML
        group: DEFAULT_GROUP  # 配置所属的组
        #我这个nacos没有密码和账户
        #username: xxxx
        #password: xxx
        namespace: a730ba07-67fc-4709-8204-7f1f6a63e00c  # Nacos命名空间的ID
        # 读取多个YAML配置，指定要加载的配置文件
        # 如加载cloudlottery-report.yml和test.yml
        extension-configs:
          - data-id: cloudlottery-report.yml  # 配置文件的ID
            group: DEFAULT_GROUP  # 配置文件所属的组
            refresh: true  # 是否自动刷新配置
          - data-id: test.yml  # 配置文件的ID
            group: DEFAULT_GROUP  # 配置文件所属的组
            refresh: true  # 是否自动刷新配置

```

# 2.nacos本地配置

![image-20240815170049293](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20240815170049293.png)

如本地后台网页地址是http://localhost:8848/naco

2.1nacos本地cloudlottery-report.yml配置

```yml
### NACOS配置

spring:
  #### 数据库配置
  datasource:
    url: jdbc:mysql://xxxx6/xxx?autoReconnect=true&useSSL=false&characterEncoding=utf-8&allowMultiQueries=true
    username: root
    password: xxxx

ribbon:
  #### 请求处理的超时时间
  ReadTimeout: 15000
  #### 请求连接的超时时间
  ConnectTimeout: 15000

#### swagger配置
swagger:
  enable: true
  title: 智能售彩湖北测试环境
  websiteUrl: http://www.szyh-smart.com
  author: 神州云海
  eamil: jianghui@szyh-smart.com
  version: 1.0.0

#### 日志配置
logging:
  output:
    size: 200
  level:
    com.platform.cloudlottery: WARN
    org.springframework: WARN
    org.spring.springboot.dao: WARN
    org.apache.coyote.http11.Http11InputBuffer: WARN
```

2.2nacos本地配置

```yml
#这个随便配的
string:
  text: 我是dev环境
  age: 20
  address: 湖南5
```

# 3.参考项目

nacos-demo-2024-08-15

# 4.读取配置

```java
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
```

```java
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
```

4.1测试如http://localhost:8082/nacosDemo/api/message

```yml
server:
  servlet:
    context-path: /nacosDemo #项目路径
  port: 8082 #端口号 
```

# 5.多渠道配置

## 方式一

```yml
spring:
  application:
    name: nacosDemo
  profiles:
    active: @profiles.active@
```

## 方式二

```yml
spring:
  application:
    name: nacosDemo
  profiles:
     active: pro
#    active: dev 
```

### 第一步:

如果在bootstrap.yml需要建立bootstrap-dev.yml，bootstrap-pro.yml等配置

### 第二步:

在pom.xml中添加

```xml
<properties> 
    <profiles.active>dev</profiles.active> <!-- 默认环境 -->
</properties>
```

```xml
<profiles>
    <profile>
        <id>dev</id>
        <properties>
            <profiles.active>dev</profiles.active>
        </properties>
    </profile>
    <profile>
        <id>pro</id>
        <properties>
            <profiles.active>pro</profiles.active>
        </properties>
    </profile>
</profiles>
```

```xml
<build>
 <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <excludes>
                    <exclude>**/*.java</exclude>
                </excludes>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>

    </build>
```

查看pom.xml文件中的打包配置，<resource>中是否加了<filtering>true<filtering>，参考

https://www.cnblogs.com/SIMPLE-zhang/p/16522455.html