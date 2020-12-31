package com.mljr.net.http.config;

import interception.MljrClientHttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 连接池和超时配置
 * @Date : 2019/3/29 18:39
 * @Author : 樊康康-(kangkang.fan@mljr.com)
 */
@Configuration
public class RestTemplateWithPoolConfigure {

    @Autowired(required = false)
    private List<MljrClientHttpRequestInterceptor> clientHttpRequestInterceptors;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        if(!CollectionUtils.isEmpty(clientHttpRequestInterceptors)) {
            List<ClientHttpRequestInterceptor> collect = clientHttpRequestInterceptors.stream().map(i -> (ClientHttpRequestInterceptor) i).collect(Collectors.toList());
            restTemplate.setInterceptors(collect);
        }
        return restTemplate;
    }

    /**
     * 设置连接池
     * @return
     */
    @Bean
    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 连接池最大连接数，根据业务配置
        poolingHttpClientConnectionManager.setMaxTotal(1000);
        // 每个主机的并发，根据业务配置
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);
        return poolingHttpClientConnectionManager;
    }

    /**
     * 设置HttpClient
     * @return
     */
    @Bean
    public HttpClient httpClient() {
        // 根据业务配置
        int timeout = 5000;
        RequestConfig config = RequestConfig.custom()
                // 连接超时时间
                .setConnectTimeout(timeout)
                // 连接池获取连接超时时间
                .setConnectionRequestTimeout(timeout)
                // 读取数据超时时间
                .setSocketTimeout(timeout)
                .build();

        return HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                // 设置连接池
                .setConnectionManager(poolingConnectionManager())
                .build();
    }

    /**
     * 创建Apache Http Components Client
     * @return
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient());
        return clientHttpRequestFactory;
    }
}
