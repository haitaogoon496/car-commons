package com.mljr.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 用于修饰对相关用@Component 修饰的Bean相关方法进行日志输出
 * @Date : 2018/4/21 下午4:11
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogMonitor {
    /**
     * 模块名称
     * 请注意，如果不定义请使用value值
     * @return
     */
    String value() default "未知模块";
    /**
     * 动作
     * @return
     */
    Action action() default @Action();

    /**
     * 需要告警的超时时间，单位 s
     * @return
     */
    int timeoutSecond() default 5;
}
