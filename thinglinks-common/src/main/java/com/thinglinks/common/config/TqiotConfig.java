package com.thinglinks.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 途强智能平台配置类
 * 绑定application.yml中tqiot前缀的配置项，统一管理平台接口配置
 */
@Component
@ConfigurationProperties(prefix = "tqiot")
@Data  // 新增Lombok注解，自动生成getter/setter等方法
public class TqiotConfig {

    /** 接口基础地址 */
    private String baseUrl;

    /** 平台登录账号 */
    private String username;

    /** 平台登录密码 */
    private String password;

    /** 平台访问AK（身份认证） */
    private String accessKey;

    /** 平台应用SK（签名生成） */
    private String accessSecret;

    /** 获取Token的接口路径 */
    private String tokenUrl;

    /** Token有效期（秒） */
    private Integer expiresSecond;

    /** 设备分页查询接口路径 */
    private String devicePagePath;

    /** 设备详情查询接口路径（含{deviceId}占位符） */
    private String deviceDetailPath;

    /** 设备状态查询接口路径（含{deviceId}占位符） */
    private String deviceStatusPath;

    /** 设备属性查询接口路径（含{deviceId}占位符） */
    private String deviceAttributePath;
}