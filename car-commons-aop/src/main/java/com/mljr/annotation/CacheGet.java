package com.mljr.annotation;

import com.mljr.cacheenum.CacheEnum;

import java.lang.annotation.*;

/**
 * @Author：rongss
 * @Description 用于获取Hash，String 的redis值
 * @Date：Created in 10:08 AM 2019/2/28
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheGet {
    /**
     * key
     * @return
     */
    String key();

    /**
     * 参数
     * @return
     */
    String paramKey() default "";

    /**
     * redis存储类型
     * @return
     */
    CacheEnum type() default CacheEnum.HASH;

    /**
     * 返回类型
     * @return
     */
    Class clazz();
}
