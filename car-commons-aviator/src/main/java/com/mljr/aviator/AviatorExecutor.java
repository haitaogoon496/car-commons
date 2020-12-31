package com.mljr.aviator;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.mljr.aviator.function.Ceil;
import com.mljr.aviator.function.Floor;
import com.mljr.aviator.function.In;
import com.mljr.aviator.function.Max;
import com.mljr.aviator.function.Min;
import com.mljr.aviator.function.Notin;
import com.mljr.aviator.function.Nvl;
import com.mljr.aviator.function.Round;
import com.mljr.aviator.function.Scale;
import com.mljr.aviator.function.Sum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @description: Aviator引擎执行器
 * @Date : 2018/9/7 下午3:21
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Slf4j
public final class AviatorExecutor {
    static {
        AviatorEvaluator.setOption(Options.ALWAYS_USE_DOUBLE_AS_DECIMAL, true);
        AviatorEvaluator.setOption(Options.MATH_CONTEXT, MathContext.DECIMAL128);
        AviatorEvaluator.addFunction(new Nvl());
        AviatorEvaluator.addFunction(new Sum());
        AviatorEvaluator.addFunction(new Min());
        AviatorEvaluator.addFunction(new Max());
        AviatorEvaluator.addFunction(new Round());
        AviatorEvaluator.addFunction(new Ceil());
        AviatorEvaluator.addFunction(new Floor());
        AviatorEvaluator.addFunction(new Scale());
        AviatorEvaluator.addFunction(new In());
        AviatorEvaluator.addFunction(new Notin());
    }

    private AviatorExecutor(){}
    /**
     * 执行结果
     * @param context 上下文对象
     * @return
     */
    public static Object execute(AviatorContext context){
        Object result = AviatorEvaluator.execute(context.getExpression(), context.getEnv(), context.isCached());
        log.info("Aviator执行器context={},result={}", JSONObject.toJSON(context),result);
        return result;
    }

    /**
     * 执行结果，返回boolean类型
     * @param context 上下文对象
     * @return
     */
    public static boolean executeBoolean(AviatorContext context){
        return (Boolean) execute(context);
    }

    /**
     * 执行结果，返回double类型
     * @param context 上下文对象
     * @return
     */
    public static double executeDouble(AviatorContext context){
        return Double.valueOf(execute(context).toString());
    }

    /**
     * 执行结果，返回BigDecimal类型
     * @param context 上下文对象
     * @return
     */
    public static BigDecimal executeBigDecimal(AviatorContext context){
        return new BigDecimal(execute(context).toString());
    }

    /**
     * 执行结果，返回BigDecimal类型
     * @param context 上下文对象
     * @return
     */
    public static String executeString(AviatorContext context){
        return (String)execute(context);
    }
}
