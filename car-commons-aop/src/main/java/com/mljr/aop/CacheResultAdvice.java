package com.mljr.aop;

import com.alibaba.fastjson.JSON;
import com.mljr.annotation.CacheResult;
import com.mljr.redis.service.RedisUtil;
import com.mljr.util.StringTools;
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

/**
 * @description: 基于Redis String类型存储的把方法结果进行Cache
 * @Date : 2019/1/22 下午2:09
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 2)
@Slf4j
public class CacheResultAdvice {

    @Autowired
    RedisUtil redisUtil;

    @Pointcut("@annotation(com.mljr.annotation.CacheResult)")
    private void annotationPoint() {
    }

    @Around("annotationPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Object proceedResult = null;
        String prefix = null;
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        try {
            CacheResult annotation = method.getAnnotation(CacheResult.class);
            prefix = annotation.prefix();
            long expire = annotation.expire();
            String cacheValue = redisUtil.get(redisUtil.getKeyWithSystemCode(prefix),String.class);
            if(StringTools.isEmpty(cacheValue)){
                proceedResult = joinPoint.proceed();
                String redisKey = redisUtil.getKeyWithSystemCode(prefix);
                redisUtil.set(redisKey, JSON.toJSONString(proceedResult),expire);
            } else {
                proceedResult = JSON.parseObject(cacheValue, method.getGenericReturnType());
            }
        } catch (Throwable throwable) {
            log.error("[CacheResultAdvice]:prefix->{}, method->{}, args->{}", prefix, method, JSON.toJSONString(joinPoint.getArgs()), throwable);
        }
        return proceedResult;
    }
}
