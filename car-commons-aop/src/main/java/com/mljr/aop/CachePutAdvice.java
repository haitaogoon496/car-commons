package com.mljr.aop;

import com.alibaba.fastjson.JSONObject;
import com.mljr.annotation.CachePut;
import com.mljr.cacheenum.CacheEnum;
import com.mljr.redis.service.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author：rongss
 * @Description
 * @Date：Created in 10:09 AM 2019/2/28
 */
@Service
@Aspect
public class CachePutAdvice {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisUtil redisUtil;

    @Pointcut("@annotation(com.mljr.annotation.CachePut)")
    private void annotationPoint() {
    }

    //声明后置通知
    @AfterReturning(pointcut = "annotationPoint()", returning = "returnValue")
    public void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        try {
            //获得注解参数
            CachePut cachePut = targetMethod.getAnnotation(CachePut.class);
            /*CacheEnum cacheEnum = cachePut.type();
            if(cacheEnum.equals(CacheEnum.STRING)){
                redisUtil.set(cachePut.key()+"_"+cachePut.paramKey(),returnValue,cachePut.expire());
                return;
            }*/

            List list = new ArrayList();
            if (returnValue instanceof List) {
                list = (List) returnValue;
            } else {
                list.add(returnValue);
            }
            putValue(list, cachePut);
        } catch (Exception e) {
            LOGGER.warn("CachePut --> targetMethod is error returnValue={}", JSONObject.toJSONString(returnValue));
        }
    }

    private void putValue(List list, CachePut cachePut) throws Exception {
        String key = cachePut.key();
        String paramKey = cachePut.paramKey();
        Long expire = cachePut.expire();
        Map<String, Object> map = new ConcurrentHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Field field = obj.getClass().getDeclaredField(paramKey);
            field.setAccessible(true);
            String value = field.get(obj).toString();
            map.put(value, JSONObject.toJSONString(obj));
        }
        redisUtil.hashOperations().putAll(key, map);
        if(expire > -1){
            redisUtil.hashOperations().getOperations().expire(key,expire, TimeUnit.SECONDS);
        }
    }

}
