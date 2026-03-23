package com.thinglinks.common.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.thinglinks.common.constant.Constants;
import com.thinglinks.common.utils.StringUtils;
import org.springframework.http.MediaType;

/**
 * 通用http发送方法（支持自定义请求头）
 *
 * @author thinglinks
 */
public class HttpUtils
{
    // 1. Logger 放在最前面（编码规范）
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    // 2. 自定义请求头（实例变量，每个实例独立）
    private Map<String, String> headers = new HashMap<>();

    // 3. 添加请求头方法
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    // ========== 静态方法（兼容原有调用，内部调用实例方法） ==========
    /**
     * 向指定 URL 发送GET方法的请求（静态）
     */
    public static String sendGet(String url)
    {
        return sendGet(url, StringUtils.EMPTY);
    }

    public static String sendGet(String url, String param)
    {
        return sendGet(url, param, Constants.UTF8);
    }

    public static String sendGet(String url, String param, String contentType)
    {
        // 调用实例版doGet，复用逻辑
        return new HttpUtils().doGet(url, param, contentType);
    }

    /**
     * 向指定 URL 发送POST方法的请求（静态）
     */
    public static String sendPost(String url, String param)
    {
        return sendPost(url, param, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    public static String sendPost(String url, String param, String contentType)
    {
        // 调用实例版doPost，复用逻辑
        return new HttpUtils().doPost(url, param, contentType);
    }

    // ========== 实例方法（支持自定义请求头） ==========
    /**
     * 向指定 URL 发送GET方法的请求（实例版，支持自定义头）
     */
    public String doGet(String url) {
        return doGet(url, StringUtils.EMPTY);
    }

    public String doGet(String url, String param) {
        return doGet(url, param, Constants.UTF8);
    }

    public String doGet(String url, String param, String contentType)
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try
        {
            String urlNameString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
            log.info("doGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();

            // 设置默认请求头
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

            // 添加自定义请求头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.doGet ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.doGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.doGet IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.doGet Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求（实例版，支持自定义头）
     */
    public String doPost(String url, String param) {
        return doPost(url, param, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    public String doPost(String url, String param, String contentType)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            log.info("doPost - {}", url);
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            // 设置默认请求头
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", contentType);

            // 添加自定义请求头
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.doPost ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.doPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.doPost IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.doPost Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    // ========== SSL POST 请求（原有逻辑保留） ==========
    public static String sendSSLPost(String url, String param)
    {
        return sendSSLPost(url, param, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    public static String sendSSLPost(String url, String param, String contentType)
    {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try
        {
            log.info("sendSSLPost - {}", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while ((ret = br.readLine()) != null)
            {
                if (ret != null && !"".equals(ret.trim()))
                {
                    result.append(new String(ret.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                }
            }
            log.info("recv - {}", result);
            conn.disconnect();
            br.close();
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

    // ========== 内部信任类（原有逻辑保留） ==========
    private static class TrustAnyTrustManager implements X509TrustManager
    {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
        {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
        {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    }
}