package com.thinglinks.app.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 百度 API 配置
 */
@Data
@Configuration
public class BaiduApiConfig {

    /**
     * 百度 API 的 AK（在百度地图开放平台申请）
     * 申请地址：https://lbsyun.baidu.com/apiconsole/key#/home
     */
    @Value("${baidu.api.ak:}")
    private String ak;

    /**
     * 百度 API 的 SN（如果启用 SN 校验）
     */
    @Value("${baidu.api.sn:}")
    private String sn;

    /**
     * 行驶里程分析 API 地址
     */
    public static final String ROAD_GRADE_API_URL = "https://api.map.baidu.com/api_trackanalysis/v1/roadgrade";
}
