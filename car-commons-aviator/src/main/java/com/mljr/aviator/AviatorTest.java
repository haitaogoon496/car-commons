package com.mljr.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 基于Aviator测试类
 * @Date : 2018/9/7 下午6:00
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class AviatorTest {

    Map<String,Object> evn = new HashMap<String,Object>(){{
        put("a","1.6");
        put("b",6.44);
        put("c",3.2);
        put("d",-5.4);
    }};

    @Test
    public void nvl(){
        String expression = "nvl(a,0)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("nvl(a,0)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void sum(){
        String expression = "sum(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("sum(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void max(){
        String expression = "max(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("max(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void min(){
        String expression = "min(a,b,c,d)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("min(a,b,c,d)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void round(){
        String expression = "round(a)";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println("round(a)="+AviatorExecutor.executeBigDecimal(ctx));
    }

    @Test
    public void test1(){
        Map<String,Object> evn = new HashMap<String,Object>(){{
            put("a",3);
            put("b",2);
            put("c",5);
            put("d",-2);
        }};
        String expression = " (a > b) && (c > 0 || d > 0) ? a : b";
        System.out.println(AviatorEvaluator.execute(expression,evn));
    }

    @Test
    public void in(){
        Map<String,Object> evn = new HashMap<String,Object>(){{
            put("rateLevel",15);
        }};
        String expression = "in(rateLevel, ',' , '15')";
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(evn).cached(true).build();
        System.out.println(AviatorExecutor.executeBoolean(ctx));
    }

    @Test
    public void scale(){
        Map<String,Object> evn = new HashMap<String,Object>(){{
            put("x",15.344);
        }};
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,0,0)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,0,1)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,0,2)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,-2,1)").env(evn).cached(true).build()));
        System.out.println(AviatorExecutor.executeBigDecimal(AviatorContext.builder().expression("scale(x,-2,2)").env(evn).cached(true).build()));
    }
}
