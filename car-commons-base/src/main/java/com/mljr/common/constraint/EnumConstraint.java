package com.mljr.common.constraint;

import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 用户规约DTO相关必须枚举字段的注解约束
 * @Date : 2018/6/2 下午2:42
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Constraint(checkWith = EnumConstraintCheck.class)
public @interface EnumConstraint {
    /**
     * 是否可以为空
     * @return
     */
    boolean acceptNull() default false;
    /**
     * 枚举类
     * @return
     */
    String enumClass();
    /**
     * 校验信息
     * @return
     */
    String message() default "非法枚举值";
}
