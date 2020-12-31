package com.mljr.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 动作
 * @Date : 2018/7/20 上午10:00
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Action {
    /**
     * 动作
     * @return
     */
    String value() default "";
}
