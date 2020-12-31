package com.mljr.util;

import org.junit.Test;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 大数据接口测试
 * @Date : 2018/10/10 上午11:21
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class DataApiTest {
    //@Test
    public void dataApi(){
        try {
            String apiUrl = "http://dataapi.mljr.com/checkgps/2156906850?logo=jjGPS";
            Map<String, String> requestHeaderMap = new HashMap<String, String>(){{
                put("sign","0cb3e9749273bb7ea0b1364a54d1ac8b");
            }};
            String result = HttpUtils.doGetSSL(apiUrl,requestHeaderMap,
                    (statusCode,httpEntity) -> System.out.println(MessageFormat.format("apiUrl={0},responseStatusCode={1},responseHttpEntity={2}",apiUrl,statusCode,httpEntity)));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
