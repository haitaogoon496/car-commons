package com.mljr.aop;

import com.alibaba.fastjson.JSON;
import com.mljr.annotation.CacheParam;
import com.mljr.redis.service.RedisUtil;
import com.mljr.util.TimeTools;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @description: 基于Redis类型存储的把方法参数进行Cache
 * @Date : 2019/1/22 下午2:09
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 3)
@Slf4j
public class CacheParamAdvice {

    final String PATTERN = "yyyyMMddHHmmssSSS";

    @Autowired
    RedisUtil redisUtil;

    @Pointcut("@annotation(com.mljr.annotation.CacheParam)")
    private void annotationPoint() {}

    @Around("annotationPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Object proceedResult = null;
        String prefix = null;
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        try {
            CacheParam annotation = method.getAnnotation(CacheParam.class);
            prefix = annotation.prefix();
            long expire = annotation.expire();
            String redisKey = redisUtil.getKeyWithSystemCode(prefix);
            String hashKey = TimeTools.format(TimeTools.createNowTime(),PATTERN);
            redisUtil.hashOperations().put(redisKey,hashKey,JSON.toJSONString(args));
            if(expire > -1){
                redisUtil.hashOperations().getOperations().expire(redisKey,expire, TimeUnit.SECONDS);
            }
            proceedResult = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("[CacheParamAdvice]:prefix->{}, method->{}, args->{}", prefix, method, JSON.toJSONString(args), throwable);
        }
        return proceedResult;
    }
}
