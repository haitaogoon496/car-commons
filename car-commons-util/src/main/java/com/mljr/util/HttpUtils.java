package com.mljr.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Description: HTTP工具类
 * 该类废弃，请使用
 * {@link OkHttpUtil}
 * @ClassName: HttpUtils
 */
@Deprecated
public final class HttpUtils {

    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static final String UTF_8 = "UTF-8";
    /**
     * 链接超时
     */
    private static int connectTimeout = 15000;

    /**
     * 读取超时
     */
    private static int readTimeout = 50000;

    /**
     * @param reqUrl
     * @param parameters
     * @return
     */
    public static String doPost(String reqUrl, Map<String, String> parameters, String charset) {
        HttpURLConnection urlConn = null;
        try {
            urlConn = sendPost(reqUrl, parameters, charset);
            if (200 == urlConn.getResponseCode()) {
                String responseContent = getContent(urlConn, charset);
                logger.info(parameters + "responseContent ======== " + responseContent.substring(0, (responseContent.length() > 100 ? 100 : responseContent.length())));
                return responseContent.trim();
            } else {
                throw new RuntimeException("http responseCode is " + urlConn.getResponseCode());
            }
        } catch (IOException e) {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
            logger.error(
                    new StringBuilder(e.getMessage()).append("|").append(JSON.toJSONString(parameters,
                            SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty)).toString(),
                    e);
            throw new RuntimeException("http responseCode is not [200]");
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }
    }

    /**
     * @param reqUrl
     * @param parameters
     * @return
     */
    public static String doPost(String reqUrl, Map<String, String> parameters, Map<String, String> headerParameters, String charset) {
        HttpURLConnection urlConn = null;
        try {
            urlConn = sendPost(reqUrl, parameters, headerParameters, charset);
            if (200 == urlConn.getResponseCode()) {
                String responseContent = getContent(urlConn, charset);
                return responseContent.trim();
            } else {
                throw new RuntimeException("http responseCode is " + urlConn.getResponseCode());
            }
        } catch (IOException e) {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
            logger.error(
                    new StringBuilder(e.getMessage()).append("|").append(JSON.toJSONString(parameters,
                            SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty)).toString(),
                    e);
            throw new RuntimeException("http responseCode is not [200]");
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }
    }
    @Deprecated
    public static String doPost(String reqUrl, String body, String charset) {
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(connectTimeout);
            urlConn.setReadTimeout(readTimeout);
            urlConn.setDoOutput(true);

            byte[] b = body.getBytes(charset);
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();
            String content = getContent(urlConn, charset);
            if (200 == urlConn.getResponseCode()) {
                return content.trim();
            } else {
                throw new RuntimeException("http responseCode is " + urlConn.getResponseCode());
            }
        } catch (Exception e) {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
            logger.error(
                    new StringBuilder(e.getMessage()).append("|").append(JSON.toJSONString(body,
                            SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty)).toString(),
                    e);
            return "";
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }
    }

    /**
     * @param urlConn
     * @return
     */
    private static String getContent(HttpURLConnection urlConn, String charset) {
        try {
            String responseContent = null;
            InputStream in = urlConn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, charset));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
            return responseContent;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @param reqUrl
     * @param parameters
     * @return
     */
    private static HttpURLConnection sendPost(String reqUrl, Map<String, String> parameters, Map<String, String> headerParameters, String charset) {
        HttpURLConnection urlConn = null;
        try {
            String params = generatorParamString(parameters, charset);
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();

            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(connectTimeout);// （单位：毫秒）jdk
            urlConn.setReadTimeout(readTimeout);// （单位：毫秒）jdk 1.5换成这个,读操作超时
            for (String key : headerParameters.keySet()) {
                urlConn.addRequestProperty(key, headerParameters.get(key));
            }
            urlConn.setDoOutput(true);
            byte[] b = params.getBytes();
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return urlConn;
    }

    /**
     * @param reqUrl
     * @param parameters
     * @return
     */
    private static HttpURLConnection sendPost(String reqUrl, Map<String, String> parameters, String charset) {
        HttpURLConnection urlConn = null;
        try {
            String params = generatorParamString(parameters, charset);
            URL url = new URL(reqUrl);
            urlConn = (HttpURLConnection) url.openConnection();

            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(connectTimeout);// （单位：毫秒）jdk
            urlConn.setReadTimeout(readTimeout);// （单位：毫秒）jdk 1.5换成这个,读操作超时
            urlConn.setDoOutput(true);
            byte[] b = params.getBytes();
            urlConn.getOutputStream().write(b, 0, b.length);
            urlConn.getOutputStream().flush();
            urlConn.getOutputStream().close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return urlConn;
    }

    /**
     * 将parameters中数据转换成用"&"链接的http请求参数形式
     *
     * @param parameters
     * @return
     */
    public static String generatorParamString(Map<String, String> parameters, String charset) {
        StringBuffer params = new StringBuffer();
        if (parameters != null) {
            for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                String value = parameters.get(name);
                value = StringTools.isEmpty(value) ? "" : value;
                params.append(name + "=");
                try {
                    params.append(URLEncoder.encode(value, charset));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e.getMessage(), e);
                } catch (Exception e) {
                    String message = String.format("'%s'='%s'", name, value);
                    throw new RuntimeException(message, e);
                }

                if (iter.hasNext()) {
                    params.append("&");
                }
            }
        }
        return params.toString();
    }

    /**
     * URL转map
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        if (StringTools.isEmpty(url)) {
            return map;
        }
        String[] params=url.substring(url.indexOf('?')+1).split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 1 && StringTools.isEmpty(p[0])) {
                map.put(p[0], null);
            } else if (p.length == 2 && StringTools.isEmpty(p[0])) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * @param link
     * @param charset
     * @return
     */
    public static String doGet(String link, String charset, int timeout) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            if (200 == conn.getResponseCode()) {
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int i = 0; (i = in.read(buf)) > 0; ) {
                    out.write(buf, 0, i);
                }
                out.flush();
                String s = new String(out.toByteArray(), charset);
                return s;
            } else {
                throw new RuntimeException("http responseCode is " + conn.getResponseCode());
            }
        } catch (Exception e) {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
    }

    /**
     * SSL GET请求
     * @param apiUrl
     * @param requestHeaderMap
     * @param responseValues
     * @return
     */
    public static String doGetSSL(String apiUrl, Map<String, String> requestHeaderMap, BiConsumer<Integer,HttpEntity> responseValues) {
        // CloseableHttpClient httpClient =
        // HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        final SSLConnectionSocketFactory sslsf;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", new PlainConnectionSocketFactory()).register("https", sslsf).build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
                .build();

        HttpGet httpGet = new HttpGet(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            // 设置请求头
            if (requestHeaderMap != null) {
                for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                responseValues.accept(statusCode,null);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                responseValues.accept(statusCode,null);
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * SSL GET请求
     * @param apiUrl
     * @param requestHeaderMap
     * @return
     */
    public static String doGetSSL(String apiUrl, Map<String, String> requestHeaderMap) {
        return doGetSSL(apiUrl,requestHeaderMap,(statusCode,httpEntity) -> logger.warn("apiUrl={},responseStatusCode={},responseHttpEntity={}",apiUrl,statusCode,httpEntity));
    }

    public static String post(String url, String jsonParams, Map<String, String> headers) {
        String output = null;
        HttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            request.setHeader("Accept", "*/*");
            request.setHeader("Content-Type", "application/json");
            // add request header
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // add request body
            if (!StringTools.isEmpty(jsonParams)) {
                StringEntity input = new StringEntity(jsonParams, UTF_8);
                request.setEntity(input);
            }
            logger.debug("request url={}, params={}", url, jsonParams);
            response = httpClient.execute(request);
            output = EntityUtils.toString(response.getEntity(), UTF_8);
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException("POST调用异常");
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            logger.warn("POST调用异常，返回code : {}", response.getStatusLine().getStatusCode());
            logger.warn("url={}, params={}", url, jsonParams);
            throw new RuntimeException("非200状态码,调用方异常.状态码为："+response.getStatusLine().getStatusCode()); //当前针对短信发送,只处理此状态码
        }

        logger.debug("response {}", output);

        return output;
    }

    /**
     * @param link
     * @return
     */
    public static String doGet(String link, String charset) {
        return doGet(link, charset, readTimeout);
    }

    public static String doGet(String link, int timeout, String charset) {
        return doGet(link, charset, timeout);
    }

    /**
     * url 编码
     *
     * @param url
     * @param enc
     * @return
     */
    public static String urlEncoding(String url, String enc) {
        try {
            return URLEncoder.encode(url, enc);
        } catch (UnsupportedEncodingException e) {
            logger.error("urlEncoding 异常 url={},enc={},e={}" + e.getMessage(), url, enc, e);
        }
        return null;
    }

    // ============域访问器=================
    public static int getConnectTimeout() {
        return connectTimeout;
    }

    public static void setConnectTimeout(int connectTimeout) {
        HttpUtils.connectTimeout = connectTimeout;
    }

    public static int getReadTimeout() {
        return readTimeout;
    }

    public static void setReadTimeout(int readTimeout) {
        HttpUtils.readTimeout = readTimeout;
    }

    public static String getRemoteIP(HttpServletRequest request) {
        // 取代理ip地址
        String ip = request.getHeader("x-forwarded-for");
        // 取nginx代理设置的ip地址
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        // 从网上取的
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        // 取JAVA获得的ip地址
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 去除unkonwn
        if (ip.startsWith("unknown")) {
            ip = ip.substring(ip.indexOf("unknown") + "unknown".length());
        }
        // 去除多多余的信息
        ip = ip.trim();
        if (ip.startsWith(",")) {
            ip = ip.substring(1);
        }
        int comma = ip.indexOf(",");
        if (comma > 0) {
            ip = ip.substring(0, comma);
        }
        return ip;
    }

    /**
     * 获取请求参数
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request){
        // 获得request参数和值
        Map<String, String> map = new HashMap<>();
        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }

}
