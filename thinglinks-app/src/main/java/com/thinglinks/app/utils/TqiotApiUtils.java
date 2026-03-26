package com.thinglinks.app.utils;

import com.alibaba.fastjson2.JSONObject;
import com.thinglinks.common.config.TqiotConfig;
import com.thinglinks.app.domain.dto.DeviceAttributeDTO;
import com.thinglinks.app.domain.dto.DeviceBasicDTO;
import com.thinglinks.app.domain.dto.DeviceStatusDTO;
import com.thinglinks.app.domain.dto.page.DevicePageRequest;
import com.thinglinks.app.domain.dto.page.DevicePageResponse;
import com.thinglinks.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 途强智能平台 API 工具类（AK/SK签名版）
 * 作用：封装Token获取、业务接口调用逻辑
 */
@Component
public class TqiotApiUtils {
    @Autowired
    private TqiotConfig tqiotConfig;

    /**
     * 生成签名：md5(accessKey+accessSecret+expiresSecond)
     * @return 小写的MD5签名
     */
    private String generateSign() {
        try {
            // 拼接签名源串：AK + SK + 有效期（顺序不能错）
            String signSource = tqiotConfig.getAccessKey() + tqiotConfig.getAccessSecret() + tqiotConfig.getExpiresSecond();
            // MD5加密
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(signSource.getBytes());
            // 转小写十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成签名失败：" + e.getMessage(), e);
        }
    }

    /**
     * 获取Token（适配AK/SK签名方式，符合平台接口文档）
     * @return 认证令牌token
     */
    public String getAccessToken() {
        try {
            // 1. 构造请求参数（严格按平台文档要求）
            JSONObject requestParam = new JSONObject();
            requestParam.put("accessKey", tqiotConfig.getAccessKey());
            requestParam.put("sign", generateSign()); // 生成MD5签名
            requestParam.put("expiresSecond", tqiotConfig.getExpiresSecond());

            // 2. 拼接Token接口完整地址
            String url = tqiotConfig.getBaseUrl() + tqiotConfig.getTokenUrl();
            System.out.println("Token请求地址：" + url);
            System.out.println("Token请求参数：" + requestParam);

            // 3. 发送POST请求获取Token（指定Content-Type为application/json）
            String responseStr = HttpUtils.sendPost(url, requestParam.toJSONString(), "application/json");
            System.out.println("Token接口响应：" + responseStr); // 打印响应便于调试

            // 4. 解析响应（按平台文档格式）
            JSONObject responseJson = JSONObject.parseObject(responseStr);
            // 校验业务成功码
            if ("00000".equals(responseJson.getString("code"))) {
                JSONObject data = responseJson.getJSONObject("data");
                String token = data.getString("token");
                System.out.println("Token获取成功，有效期：" + tqiotConfig.getExpiresSecond() + "秒");
                return token;
            } else {
                // 提取错误信息（msg/desc都可能为null，做兜底）
                String msg = responseJson.getString("msg") == null ? "无错误信息" : responseJson.getString("msg");
                String desc = responseJson.getString("desc") == null ? "无详细描述" : responseJson.getString("desc");
                throw new RuntimeException("获取Token失败：" + msg + "，详细描述：" + desc);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用Token接口异常：" + e.getMessage(), e);
        }
    }

    public DevicePageResponse queryDevicePage(Long pageIndex, Long pageSize, DevicePageRequest.DeviceQueryData queryData) {
        try {
            // 参数校验
            if (pageIndex == null || pageIndex < 1) {
                throw new IllegalArgumentException("页码pageIndex必须≥1");
            }
            if (pageSize == null || pageSize < 1 || pageSize > 200) {
                throw new IllegalArgumentException("每页条数pageSize必须在1-200之间");
            }

            // 获取Token + 构造参数
            String token = getAccessToken();
            DevicePageRequest request = new DevicePageRequest();
            request.setPageIndex(pageIndex);
            request.setPageSize(pageSize);
            if (queryData == null) {
                queryData = new DevicePageRequest.DeviceQueryData();
                queryData.setDeptSubordinate(false);
            }
            request.setData(queryData);

            // 拼接地址（使用配置类路径）
            String devicePageUrl = tqiotConfig.getBaseUrl() + tqiotConfig.getDevicePagePath();

            // 发送请求
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.addHeader("token", token);
            httpUtils.addHeader("Content-Type", "application/json");
            String requestJson = JSONObject.toJSONString(request);
            String responseStr = httpUtils.doPost(devicePageUrl, requestJson, "application/json");

            // 解析响应
            DevicePageResponse response = JSONObject.parseObject(responseStr, DevicePageResponse.class);
            if (!"00000".equals(response.getCode())) {
                throw new RuntimeException("设备查询失败：" + response.getMsg());
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException("调用设备分页接口异常：" + e.getMessage(), e);
        }
    }

    /**
     * 简化版分页查询
     */
    public DevicePageResponse queryDevicePage() {
        return queryDevicePage(1L, 10L, null);
    }

    // ---------------- 通用 GET 请求（抽成公共方法） ----------------
    public <T> T doGet(String path, Class<T> clazz, String... headers) {
        try {
            String token = getAccessToken();
            String url = tqiotConfig.getBaseUrl() + path;

            HttpUtils http = new HttpUtils();
            http.addHeader("token", token);
            http.addHeader("Content-Type", "application/json");

            // 传入自定义 header：deviceId 等
            if (headers != null && headers.length >= 2) {
                for (int i = 0; i < headers.length; i += 2) {
                    http.addHeader(headers[i], headers[i+1]);
                }
            }

            String resp = http.doGet(url);
            return JSONObject.parseObject(resp, clazz);
        } catch (Exception e) {
            throw new RuntimeException("GET 请求失败：" + e.getMessage());
        }
    }
}