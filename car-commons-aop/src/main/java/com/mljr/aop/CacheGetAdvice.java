package com.mljr.aop;

import com.alibaba.fastjson.JSONObject;
import com.mljr.annotation.CacheGet;
import com.mljr.cacheenum.CacheEnum;
import com.mljr.redis.service.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

import java.lang.reflect.Method;

/**
 * @Author：rongss
 * @Description
 * @Date：Created in 10:09 AM 2019/2/28
 */
@Component
@Aspect
public class CacheGetAdvice {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Autowired
    RedisUtil redisUtil;

    @Pointcut("@annotation(com.mljr.annotation.CacheGet)")
    private void annotationPoint() {}

    @Around("annotationPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        try{
            //获得注解参数
            CacheGet cacheGet = targetMethod.getAnnotation(CacheGet.class);
            //反射获取paramKey
            Object[] args = joinPoint.getArgs();
            Object arg = args[0];

            //获得解析数据
            Object result = getCacheValue(cacheGet,arg);
            if(StringUtils.isEmpty(result)){
                return joinPoint.proceed();
            }
            return result;
        }catch (Exception e){
            LOGGER.warn("methodName --> {} CacheGet is error",targetMethod.getName(),e);
            return joinPoint.proceed();
        }
    }

    public Object getCacheValue(CacheGet cacheGet,Object arg)throws Exception{

        Object re = "";
        String key = cacheGet.key();
        String paramKey = cacheGet.paramKey();
        CacheEnum cacheEnum = cacheGet.type();
        Class clazz = cacheGet.clazz();

        switch (cacheEnum){
            case HASH:
                Field field = arg.getClass().getDeclaredField(paramKey);
                field.setAccessible(true);
                String value = field.get(arg).toString();
                re =redisUtil.hashOperations().get(key,value);
                return JSONObject.parseObject(re.toString(),clazz);
           /* case STRING:
                re = redisUtil.get(arg.toString(),String.class);
                return re;*/
            default:
                return re;
        }
    }
}
