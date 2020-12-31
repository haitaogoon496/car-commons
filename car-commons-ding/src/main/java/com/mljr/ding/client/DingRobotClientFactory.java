package com.mljr.ding.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 钉钉工厂类
 * @Date : 2018/9/27 下午2:14
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public final class DingRobotClientFactory {
    /**
     * 享元Map，存储DingRobotFactory的实例容器
     */
    public static final Map<String,DingRobotService> SHARE_MAP = new ConcurrentHashMap<>();
    /**
     * client创建
     * @param key
     * @return
     */
    public static DingRobotService create(String key){
        if (null == key || "".equals(key)) {
            throw new RuntimeException("参数key不能为空");
        }
        DingRobotService client = SHARE_MAP.get(key);
        if(null == client){
            client = new DingRobotClient();
            SHARE_MAP.put(key,client);
        }
        return client;
    }

    private DingRobotClientFactory(){}
}
