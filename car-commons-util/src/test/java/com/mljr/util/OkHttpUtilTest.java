package com.mljr.util;

import org.junit.Test;

import java.io.IOException;

/**
 * @description: OkHttpTest
 * @Date : 2018/10/4 上午11:56
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class OkHttpUtilTest {
    @Test
    public void test1(){
        try {
            String url = "https://blog.csdn.net/bluishglc/article/details/7696085";
            String result = OkHttpUtil.get(url);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
