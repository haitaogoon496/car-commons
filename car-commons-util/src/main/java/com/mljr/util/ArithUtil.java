package com.mljr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 数字计算精度、进位方式的处理
 * @Date : 下午3:51 2018/4/15
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public final class ArithUtil {
    private static final Logger logger = LoggerFactory.getLogger(ArithUtil.class);
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE=10;
    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 提供精确的小数位四舍五入处理。
     * @param v  需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 如果是整数位的进位，则调用此方法。
     * @param value
     * @param roundType
     * @param scale
     * @return
     */
    public  static BigDecimal zhengshuRound(BigDecimal value , int roundType , int scale){
        //解释：我们要将423.5 精确到百位，先将423.5/100变成4.235， 然后将4.235进行取整后为4，最后将4*100=400， 则为最后结果。
        if(scale<0){
            Double temp = Math.pow(10,Math.abs(scale));
            BigDecimal jinWei = new BigDecimal(temp.intValue());
            value = value.divide(jinWei, 16, BigDecimal.ROUND_HALF_UP).setScale(0,getRoundType(roundType)).multiply(jinWei);
            return value;
        }else {
            throw new RuntimeException("参数 scale 不符合要求！");
        }
    }

    /**
     * 获取取证方式
     * @param roundType
     * @return
     */
    public static int getRoundType(int roundType){
        switch (roundType){
            case 0:
                return BigDecimal.ROUND_HALF_UP;
            case 1:
                return BigDecimal.ROUND_UP;
            case 2:
                return BigDecimal.ROUND_DOWN;
            default:
                return BigDecimal.ROUND_HALF_UP;
        }
    }

    /**
     * 对value精度和取整方式处理
     * @param value 原值
     * @param scale 取整方式
     * @param roundType 精度枚举
     */
    public static BigDecimal truncate(BigDecimal value, int scale, int roundType) {
        if(scale >= 0) {
            value = value.setScale(scale, getRoundType(roundType));
        }else{
            value = zhengshuRound(value,roundType,scale);
        }
        return value;
    }

    /**
     * @param value
     * @return
     * @Description: Object类型数据转BigDecimal
     */
    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(value.toString());
            } else {
                logger.error("Not possible to coerce {} from class {} into a BigDecimal", value, value.getClass());
            }
        }
        return  ret;
    }
}
