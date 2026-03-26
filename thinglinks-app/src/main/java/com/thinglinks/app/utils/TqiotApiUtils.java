package com.thinglinks.app.utils;

import com.alibaba.fastjson2.JSONObject;
import com.thinglinks.common.config.TqiotConfig;
import com.thinglinks.app.domain.dto.page.DevicePageRequest;
import com.thinglinks.app.domain.dto.page.DevicePageResponse;
import com.thinglinks.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class TqiotApiUtils {

    @Autowired
    private TqiotConfig tqiotConfig;

    /**
     * 生成签名
     */
    private String generateSign() {
        try {
            String s = tqiotConfig.getAccessKey() + tqiotConfig.getAccessSecret() + tqiotConfig.getExpiresSecond();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("签名失败");
        }
    }

    /**
     * 获取AccessToken
     */
    public String getAccessToken() {
        JSONObject param = new JSONObject();
        param.put("accessKey", tqiotConfig.getAccessKey());
        param.put("sign", generateSign());
        param.put("expiresSecond", tqiotConfig.getExpiresSecond());

        String url = tqiotConfig.getBaseUrl() + tqiotConfig.getTokenUrl();
        String resp = HttpUtils.sendPost(url, param.toJSONString(), "application/json");

        JSONObject json = JSONObject.parseObject(resp);
        if (!"00000".equals(json.getString("code"))) {
            throw new RuntimeException("获取Token失败：" + json.getString("msg"));
        }
        return json.getJSONObject("data").getString("token");
    }

    /**
     * 设备分页查询（自定义条件）
     */
    public DevicePageResponse queryDevicePage(Long pageIndex, Long pageSize, DevicePageRequest.DeviceQueryData queryData) {
        if (pageIndex < 1 || pageSize < 1 || pageSize > 200) {
            throw new IllegalArgumentException("分页参数非法");
        }

        DevicePageRequest req = new DevicePageRequest();
        req.setPageIndex(pageIndex);
        req.setPageSize(pageSize);
        if (queryData == null) {
            queryData = new DevicePageRequest.DeviceQueryData();
            queryData.setDeptSubordinate(false);
        }
        req.setData(queryData);

        HttpUtils http = createHttp();
        String url = tqiotConfig.getBaseUrl() + tqiotConfig.getDevicePagePath();
        String resp = http.doPost(url, JSONObject.toJSONString(req), "application/json");
        return JSONObject.parseObject(resp, DevicePageResponse.class);
    }

    /**
     * 简化版分页查询
     */
    public DevicePageResponse queryDevicePage() {
        return queryDevicePage(1L, 10L, null);
    }

    /**
     * 通用GET请求
     */
    public <T> T doGet(String path, Class<T> clazz, String... headers) {
        HttpUtils http = createHttp();
        if (headers != null && headers.length >= 2) {
            for (int i = 0; i < headers.length; i += 2) {
                http.addHeader(headers[i], headers[i + 1]);
            }
        }

        String url = tqiotConfig.getBaseUrl() + path;
        String resp = http.doGet(url);
        return JSONObject.parseObject(resp, clazz);
    }

    /**
     * 统一创建Http请求对象
     */
    private HttpUtils createHttp() {
        HttpUtils http = new HttpUtils();
        http.addHeader("token", getAccessToken());
        http.addHeader("Content-Type", "application/json");
        return http;
    }
}