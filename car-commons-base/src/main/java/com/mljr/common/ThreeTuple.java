package com.mljr.common;

/**
 * @description: Tuple容器类
 * @Date : 上午10:36 2017/9/28
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public class ThreeTuple<A,B,C> {
    private A a;
    private B b;
    private C c;

    private ThreeTuple() {
    }

    public ThreeTuple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }

    /**
     * 静态方法，返回一个容器实例对象
     * @param a 容器元素1
     * @param b 容器元素2
     * @param c 容器元素3
     * @param <A>
     * @param <B>
     * @param <C>
     * @return
     */
    public static <A,B,C> ThreeTuple<A,B,C> newInstance(A a, B b, C c){
        return new ThreeTuple(a,b,c);
    }

    @Override
    public String toString() {
        return "ThreeTuple{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
