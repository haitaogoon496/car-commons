package com.mljr.common;

/**
 * @description: Tuple容器类
 * @Date : 上午10:33 2017/9/28
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public class Tuple<A> {

    private A a;

    private Tuple() {
    }

    public Tuple(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }

    /**
     * 静态方法，返回一个容器实例对象
     * @param a 容器元素
     * @param <A>
     * @return
     */
    public static <A> Tuple<A> newInstance(A a){
        return new Tuple<>(a);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "a=" + a +
                '}';
    }
}
