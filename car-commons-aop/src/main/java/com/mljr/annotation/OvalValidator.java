package com.mljr.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 基于oval的注解的DTO约束校验注解
 * @Date : 2018/6/2 下午2:32
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OvalValidator {
    /**
     * 模块名称
     * 请注意，如果不定义请使用module值
     * @return
     */
    String value() default "未知模块";
    /**
     * 动作
     * @return
     */
    Action action() default @Action();
}
