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

    /**
     * 设备分页查询（使用配置类路径）
     */
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

    /**
     * 获取设备详情（使用配置类路径）
     */
    public DeviceBasicDTO getDeviceDetail(String deviceId) {
        try {
            // 参数校验
            if (deviceId == null || deviceId.trim().isEmpty()) {
                throw new IllegalArgumentException("设备ID(deviceId)不能为空");
            }

            // 获取Token + 拼接地址（替换占位符）
            String token = getAccessToken();
            String apiPath = tqiotConfig.getDeviceDetailPath().replace("{deviceId}", deviceId);
            String url = tqiotConfig.getBaseUrl() + apiPath;

            // 发送GET请求
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.addHeader("token", token);
            httpUtils.addHeader("Content-Type", "application/json");
            String responseStr = httpUtils.doGet(url);

            // 4. 解析响应（核心改造：转换为DeviceBasicDTO）
            DeviceBasicDTO responseDTO = JSONObject.parseObject(responseStr, DeviceBasicDTO.class);

            // 5. 校验响应码（保持原有异常逻辑）
            if (!"00000".equals(responseDTO.getCode())) {
                throw new RuntimeException("获取设备详情失败：" + responseDTO.getMsg());
            }

            // 6. 返回完整DTO（也可按需仅返回data字段，看业务需求）
            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException("调用设备详情接口异常：" + e.getMessage(), e);
        }
    }

    /**
     * @param deviceId 设备ID
     * @return 设备状态信息JSON对象
     */
    public DeviceStatusDTO getDeviceStatus(String deviceId) {
        try {
            // 参数校验
            if (deviceId == null || deviceId.trim().isEmpty()) {
                throw new IllegalArgumentException("设备ID(deviceId)不能为空");
            }

            // 获取Token + 拼接地址（替换占位符）
            String token = getAccessToken();
            String apiPath = tqiotConfig.getDeviceStatusPath().replace("{deviceId}", deviceId);
            String url = tqiotConfig.getBaseUrl() + apiPath;
            System.out.println("设备状态请求地址：" + url);

            // 发送GET请求
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.addHeader("token", token);
            httpUtils.addHeader("Content-Type", "application/json");
            String responseStr = httpUtils.doGet(url);
            System.out.println("设备状态接口响应：" + responseStr);

            // 解析响应
            DeviceStatusDTO responseDTO = JSONObject.parseObject(responseStr, DeviceStatusDTO.class);
            if (!"00000".equals(responseDTO.getCode())) {
                String msg = responseDTO.getMsg() == null ? "无错误信息" : responseDTO.getMsg();
                String desc = responseDTO.getDesc() == null ? "无详细描述" : responseDTO.getDesc();
                throw new RuntimeException("获取设备状态失败：" + msg + "，详细描述：" + desc);
            }
            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException("调用设备状态接口异常：" + e.getMessage(), e);
        }
    }

    /**
     * 获取设备属性信息（ICCID、车架号等）
     */
    public DeviceAttributeDTO getDeviceAttribute(String deviceId) {
        try {
            // 1. 参数校验
            if (deviceId == null || deviceId.trim().isEmpty()) {
                throw new IllegalArgumentException("设备ID(deviceId)不能为空");
            }

            // 2. 获取Token + 直接拼接URL
            String token = getAccessToken();
            String apiPath = tqiotConfig.getDeviceAttributePath().replace("{deviceId}", deviceId);
            String url = tqiotConfig.getBaseUrl() + apiPath; // 直接拼接，无工具方法
            System.out.println("设备属性请求地址：" + url);

            // 3. 发送GET请求
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.addHeader("token", token);
            httpUtils.addHeader("Content-Type", "application/json");
            String responseStr = httpUtils.doGet(url);
            System.out.println("设备属性接口响应：" + responseStr);

            // 4. 解析响应
            DeviceAttributeDTO responseDTO = JSONObject.parseObject(responseStr, DeviceAttributeDTO.class);
            if (!"00000".equals(responseDTO.getCode())) {
                String msg = responseDTO.getMsg() == null ? "无错误信息" : responseDTO.getMsg();
                String desc = responseDTO.getDesc() == null ? "无详细描述" : responseDTO.getDesc();
                throw new RuntimeException("获取设备属性失败：" + msg + "，详细描述：" + desc);
            }

            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException("调用设备属性接口异常：" + e.getMessage(), e);
        }
    }
}