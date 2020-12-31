package com.mljr.common.constraint;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 用户规约DTO相关必须枚举字段的注解约束
 * @Date : 2018/6/2 下午3:00
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class EnumConstraintCheck extends AbstractAnnotationCheck<EnumConstraint> {
    protected final String methodName = "getIndex";
    @Override
    public boolean isSatisfied(Object target, Object value, OValContext ctx, Validator validator) throws OValException {
        try {
            EnumConstraint constraint = ((FieldContext) ctx).getField().getDeclaredAnnotation(EnumConstraint.class);
            String enumClass = constraint.enumClass();
            Class<?> clazz = Class.forName(enumClass);
            if(clazz.isEnum() && !constraint.acceptNull() ){
                Method  getIndex = clazz.getMethod(methodName);
                List<Integer> indexes = new ArrayList<>();
                for(Object obj : clazz.getEnumConstants()){
                    indexes.add((Integer)getIndex.invoke(obj));
                }
                return indexes.contains(Integer.valueOf(value.toString()));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return true;
    }
}
