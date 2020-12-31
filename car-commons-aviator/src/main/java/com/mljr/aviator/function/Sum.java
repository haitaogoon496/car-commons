package com.mljr.aviator.function;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description: 多个数字求和计算(其中任何一个数字为空则作为0处理)
 * @Date : 2018/9/7 下午5:40
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class Sum extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        BigDecimal sum = new BigDecimal(0);
        for(AviatorObject each : args){
            Object value = each.getValue(env);
            if(value != null){
                sum = sum.add(new BigDecimal(value.toString()));
            }
        }
        return new AviatorDecimal(new BigDecimal(sum.toString()));
    }

    @Override
    public String getName() {
        return "sum";
    }
}
