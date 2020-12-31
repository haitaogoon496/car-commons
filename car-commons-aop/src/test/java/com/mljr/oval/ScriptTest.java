package com.mljr.oval;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @description: Script测试
 * @Date : 2018/10/9 下午2:21
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class ScriptTest {
    @Test
    public void script(){
        try {
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
            System.out.println("2*6-(6+5) 结果："+scriptEngine.eval("2*6-(6+5)"));
            System.out.println("1>2?true:false 结果："+scriptEngine.eval("1>2?true:false"));
            scriptEngine.put("a","1");
            scriptEngine.put("b","5");
            scriptEngine.put("c","2");
            System.out.println("(a >b || c < b) ?  (a + b) : (a - b) 结果："+scriptEngine.eval("(a >b || c < b) ?  (a + b) : (a - b)"));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
