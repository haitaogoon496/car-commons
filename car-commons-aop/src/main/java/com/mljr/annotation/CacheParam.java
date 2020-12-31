package com.mljr.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 基于Redis存储的把方法参数进行Cache
 * @Date : 2019/1/22 下午12:06
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheParam {
    /**
     * key prefix
     * @return
     */
    String prefix();
    /**
     * 失效时间，默认永久缓存（单位：秒）
     * @return
     */
    long expire() default -1;
}
