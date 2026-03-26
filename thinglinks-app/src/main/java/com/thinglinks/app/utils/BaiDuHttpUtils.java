package com.thinglinks.app.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP 工具类
 */
@Slf4j
public class BaiDuHttpUtils {

    /**
     * 发送 POST 请求（JSON 格式）
     * @param url 请求地址
     * @param json 请求体（JSON 格式）
     * @return 响应结果
     */
    public static String postJson(String url, String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost(url);

            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");

            // 设置请求体
            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);

            // 设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(5000)
                    .build();
            httpPost.setConfig(requestConfig);

            // 发送请求
            response = httpClient.execute(httpPost);

            // 获取响应
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            log.error("HTTP POST 请求失败", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                log.error("关闭连接失败", e);
            }
        }

        return null;
    }

    /**
     * 发送 POST 请求（x-www-form-urlencoded 格式）
     * @param url 请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static String postForm(String url, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost(url);

            // 设置请求参数
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置请求体为 x-www-form-urlencoded
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));

            // 设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(5000)
                    .build();
            httpPost.setConfig(requestConfig);

            // 发送请求
            response = httpClient.execute(httpPost);

            // 获取响应
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            log.error("HTTP POST 请求失败", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                log.error("关闭连接失败", e);
            }
        }

        return null;
    }
}
