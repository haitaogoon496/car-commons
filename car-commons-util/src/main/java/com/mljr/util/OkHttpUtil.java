package com.mljr.util;

import com.alibaba.fastjson.JSON;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @description: 基于okhttp3的Http处理工具类
 * @Date : 2018/10/4 上午11:55
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public final class OkHttpUtil {
    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);
    private static final OkHttpClient client;
    private static final MediaType JSON_TYPE = MediaType.parse("application/json");
    static final Properties PROPERTIES = PropertiesReader.getProperties("config");
    static {
        try {
            int connectionTimeout = 3;
            int readTimeout = 3;
            if(null != PROPERTIES){
                connectionTimeout = Integer.valueOf(PROPERTIES.getProperty("okhttp.connectionTimeout","3"));
                readTimeout = Integer.valueOf(PROPERTIES.getProperty("okhttp.readTimeout","3"));
            }
            client = new OkHttpClient().newBuilder().connectTimeout(connectionTimeout, TimeUnit.SECONDS).readTimeout(readTimeout, TimeUnit.SECONDS).build();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("");
        }
    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @return json字符串
     */
    public static String get(String url) throws IOException {
        return get(url,Collections.emptyMap(),response -> {});
    }


    /**
     * get请求
     * @param url 请求地址
     * @param headerMap header
     * @param responseConsumer responseConsumer
     * @return json字符串
     */
    public static String get(String url,Map<String,String> headerMap,Consumer<Response> responseConsumer) throws IOException {
        long cost, start = System.currentTimeMillis();
        Request.Builder builder = new Request.Builder();
        if(null != headerMap && !headerMap.isEmpty()){
            headerMap.entrySet().forEach(header -> builder.addHeader(header.getKey(),header.getValue()));
        }
        Request request = builder.url(url).build();
        Response response = client.newCall(request).execute();
        responseConsumer.accept(response);
        if (!response.isSuccessful()) {
            cost = System.currentTimeMillis() - start;
            logger.error("[OkHttpUtil] - failure, cost={}ms, url={}, response={}", cost, url, response);
            return null;
        }
        String content = response.body().string();
        cost = System.currentTimeMillis() - start;
        logger.debug("[OkHttpUtil] - succeed, cost={}ms, url={} ", cost, url);
        return content;
    }

    /**
     * 异步http请求
     *
     * @param url
     * @param callback
     */
    public static void asyncGet(String url, Callback callback) {
        long cost, start = System.currentTimeMillis();
        try {
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(callback);
            cost = System.currentTimeMillis() - start;
            logger.debug("[OkHttpUtil] - succeed, cost={}ms, url={} ", cost, url);
        } catch (Exception e) {
            cost = System.currentTimeMillis() - start;
            logger.error("[OkHttpUtil] - exception, cost={}ms, url={},\t occur exception", cost, url, e);
        }
    }

    /**
     * get请求，并序列化返回结果
     *
     * @param clazz 返回结果类对象
     * @param url   请求地址
     * @param <T>   返回结果类型
     * @return 返回结果对象
     */
    public static <T> T get(Class<T> clazz, String url) throws IOException {
        String result = get(url);
        if (result != null) {
            return JSON.parseObject(result, clazz);
        }
        return null;
    }

    /**
     * post请求
     *
     * @param params 请求参数
     * @param url    请求地址
     * @param <T>    请求参数类型
     * @return 返回结果对象
     */
    public static <T> String post(T params, String url, String mediaType) throws IOException {
        return post(JSON.toJSONString(params), url,mediaType);
    }

    /**
     * post请求
     *
     * @param params 请求参数
     * @param url    请求地址
     * @param <T>    请求参数类型
     * @return 返回结果对象
     */
    public static <T> void asyncPost(T params, String url, Callback callback, String mediaType) {
        long cost, start = System.currentTimeMillis();
        try {
            MediaType mt = JSON_TYPE;
            if(StringTools.isNotEmpty(mediaType)){
                mt = MediaType.parse(mediaType);
            }
            RequestBody body = RequestBody.create(mt, JSON.toJSONString(params));
            Request request = new Request.Builder().url(url).post(body).build();
            client.newCall(request).enqueue(callback);
            cost = System.currentTimeMillis() - start;
            logger.debug("[OkHttpUtil] - succeed, cost={}ms, url={} ", cost, url);
        } catch (Exception e) {
            cost = System.currentTimeMillis() - start;
            logger.error("[OkHttpUtil] - exception, cost={}ms, url={},\t occur exception", cost, url, e);
        }
    }

    /**
     * post请求
     *
     * @param clazz  返回结果类对象
     * @param params 请求参数
     * @param url    请求地址
     * @param <T>    请求参数类型
     * @param <R>    返回结果类型
     * @return 返回结果对象
     */
    public static <T, R> R post(Class<R> clazz, T params, String url, String mediaType) throws IOException {
        String result = post(JSON.toJSONString(params), url, mediaType);
        if (result != null) {
            return JSON.parseObject(result, clazz);
        }
        return null;
    }

    /**
     * post请求
     *
     * @param clazz  返回结果类对象
     * @param params 请求参数
     * @param url    请求地址
     * @param <T>    请求参数类型
     * @param <R>    返回结果类型
     * @return 返回结果对象
     */
    public static <T, R> R post(Class<R> clazz, T params, String url) throws IOException {
        return post(clazz,params,url,null);
    }

    /**
     * post请求
     *
     * @param json json格式的请求参数
     * @param url  请求地址
     * @return json字符串
     */
    public static String post(String json, String url,String mediaType) throws IOException {
        return post(json,url, Collections.emptyMap(),mediaType);
    }
    /**
     * post请求
     *
     * @param json json格式的请求参数
     * @param url  请求地址
     * @param headerMap header
     * @return json字符串
     */
    public static String post(String json, String url, Map<String,String> headerMap,String mediaType) throws IOException {
        long cost, start = System.currentTimeMillis();
        MediaType mt = JSON_TYPE;
        if(StringTools.isNotEmpty(mediaType)){
            mt = MediaType.parse(mediaType);
        }
        RequestBody body = RequestBody.create(mt, json);
        Request.Builder builder = new Request.Builder();
        if(null != headerMap && !headerMap.isEmpty()){
            headerMap.entrySet().forEach(header -> builder.addHeader(header.getKey(),header.getValue()));
        }
        Request request = builder.url(url).post(body).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            cost = System.currentTimeMillis() - start;
            logger.error("[Crawler] - failure, cost={}ms, url={}, params={},responseCode={},responseMessage={}", cost, url, json,response.code(),response.message());
            return null;
        }
        String content = response.body().string();
        cost = System.currentTimeMillis() - start;
        logger.debug("[Crawler] - succeed, cost={}ms, url={}, params={}", cost, url, json);
        return content;
    }

    /**
     * post请求
     *
     * @param json json格式的请求参数
     * @param url  请求地址
     * @return json字符串
     */
    public static String post(String json, String url) throws IOException {
        return post(json,url,null);
    }


    public static void main(String[] args) {
        try {
            String tokenUrl = "http://test-discovery.paas.op.mljr.com/risk/data-platform-beta-e-svc/auth/queryToken?uuid=e2e87cb6658340d3b9cac8e1a42dc8fd";
            //tokenUrl = "http://dataapi.mljr.com/auth/queryToken?uuid=83c01f57ec8f416aa8be0ecacf0a4c99";
            String sign = OkHttpUtil.get(tokenUrl);
            Map<String,String> map = new HashMap<>(2);
            map.put("user-sign",sign);
            String url = "http://test-discovery.paas.op.mljr.com/risk/data-platform-beta-e-svc/checkgps/400410233333333333333333334324?uniqueId=15609222222&logo=segGPS";
            //url = "http://dataapi.mljr.com/checkgps/40041024324?uniqueId=1548212569609&logo=segGPS";
            String result = OkHttpUtil.get(url,map,response -> {});
            System.out.println(result);
            System.out.println(sign);
            System.out.println(1 << 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}