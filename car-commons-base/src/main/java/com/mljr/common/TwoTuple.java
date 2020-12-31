package com.mljr.common;

/**
 * @description: Tuple容器类
 * @Date : 上午10:35 2017/9/28
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public class TwoTuple<A,B> {
    private A a;
    private B b;

    private TwoTuple() {
    }

    public TwoTuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    /**
     * 静态方法，返回一个容器实例对象
     * @param a 容器元素1
     * @param b 容器元素2
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A,B> TwoTuple<A,B> newInstance(A a, B b){
        return new TwoTuple(a,b);
    }

    @Override
    public String toString() {
        return "TwoTuple{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
