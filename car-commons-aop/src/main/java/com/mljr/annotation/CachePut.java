package com.mljr.annotation;

import com.mljr.cacheenum.CacheEnum;

import java.lang.annotation.*;

/**
 * @Author：rongss
 * @Description 存储Hash和String的值
 * @Date：Created in 10:08 AM 2019/2/28
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CachePut {
    /**
     * key prefix
     * @return
     */
    String key();

    /**
     * 数据key
     * @return
     */
    String paramKey();

    /**
     * 具体值
     * @return
     */
    CacheEnum type() default CacheEnum.HASH;

    /**
     * 失效时间，默认永久缓存（单位：秒）
     * @return
     */
    long expire() default -1;


}
