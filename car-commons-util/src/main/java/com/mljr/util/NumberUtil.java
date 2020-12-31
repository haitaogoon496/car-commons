package com.mljr.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 数字类型工具类<br>
 * @version V1.0  2017/4/29 17:45  by 石冬冬-Seig Heil（dd.shi02@zuche.com）创建
 */
public final class NumberUtil {
    private static final DecimalFormat myformat = new DecimalFormat("0.00");


    /**
     * 将数字格式化成2位小数
     *
     * @param number
     * @return
     */
    public static String fmt2p(Number number) {
        return myformat.format(number);
    }

    /**
     * 判断数值o1 和数值o2是否相等
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean isEquals(Number o1, Number o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else if (o1 instanceof Double || o1 instanceof Float || o2 instanceof Double || o2 instanceof Float) {
            return o1.doubleValue() == o2.doubleValue();
        } else {
            return o1.longValue() == o2.longValue();
        }
    }

    public static Short parseShort(Object o) {
        if (o instanceof Number) {
            return ((Number) o).shortValue();
        } else if (o instanceof String) {
            try {
                return Short.parseShort((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.shortValue();
            }
        }
        return null;
    }

    public static Byte parseByte(Object o) {
        if (o instanceof Number) {
            return ((Number) o).byteValue();
        } else if (o instanceof String) {
            try {
                return Byte.parseByte((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.byteValue();
            }
        }
        return null;
    }


    public static Integer parseInt(Object o) {
        if (o instanceof Number) {
            return ((Number) o).intValue();
        } else if (o instanceof String) {
            try {
                return Integer.parseInt((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.intValue();
            }
        }
        return null;
    }

    public static Long parseLong(Object o) {
        if (o instanceof Number) {
            return ((Number) o).longValue();
        } else if (o instanceof String) {
            try {
                return Long.parseLong((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.longValue();
            }
        }
        return null;
    }

    /**
     * (Integer类型处理)如果是null 则返回0
     * @param value
     * @return int
     */
    public static int isNull0(Integer value) {
        return isNull(value, 0);
    }

    /**
     * (Double类型处理)如果是null 则返回0
     * @param value
     * @return double
     */
    public static double isNull0(Double value) {
        return isNull(value, 0.0);
    }

    /**
     * (BigDecimal类型处理)如果是null 则返回0
     * @param value
     * @return BigDecimal
     */
    public static BigDecimal isNull0(BigDecimal value) {
        return isNull(value, BigDecimal.ZERO);
    }

    /**
     * (Integer类型处理)如果是null 则返回newValue
     * @param value 原值
     * @param newValue 新值
     * @return int
     */
    public static int isNull(Integer value, int newValue) {
        return value == null ? newValue : value;
    }
    /**
     * (Double类型处理)如果是null 则返回newValue
     * @param value 原值
     * @param newValue 新值
     * @return double
     */
    public static double isNull(Double value, double newValue) {
        return value == null ? newValue : value;
    }
    /**
     * (BigDecimal类型处理)如果是null 则返回newValue
     * @param value 原值
     * @param newValue 新值
     * @return BigDecimal
     */
    public static BigDecimal isNull(BigDecimal value, BigDecimal newValue) {
        return value == null ? newValue : value;
    }

    public static Float parseFloat(Object o) {
        if (o instanceof Number) {
            return ((Number) o).floatValue();
        } else if (o instanceof String) {
            try {
                return Float.parseFloat((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.floatValue();
            }
        }
        return null;
    }

    public static Double parseDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o instanceof String) {
            try {
                return Double.parseDouble((String) o);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;

    }


    /**
     *
     * @param value 需要设置的值
     * @param scale  范围 0 表示取整 1表示保留1位小数  四舍五入
     * @return
     * create by leiting
     */
    public static Double setScale(Double value,Integer scale){
        if(null==value){
            return 0D;
        }

        if(null==scale || scale<0){
            throw new IllegalArgumentException("保留范围必须大于或者等于0");

        }

        BigDecimal   b   =   new   BigDecimal(value);
        return b.setScale(scale,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    //判断是否是几位数的的数字
    public  static Boolean isNumberAndSize(String num,int size){

        String match="^\\-?([0-9]{"+size+"})$";
        if (num.matches(match)){
            return true;
        }
        return false;
    }

    /**
     * 金额四舍五入
     * @param value 需要设置的值
     * @param scale  范围 0 表示取整 1表示保留1位小数  四舍五入
     * @return
     * create by sy.li12 李苏毅
     */
    public static String setScale(String value, Integer scale) {
        if (null == value) {
            return "0";
        }

        if (!isNumeric(value)){
            throw new IllegalArgumentException("参数必须是数字");
        }

        if (null == scale || scale < 0) {
            throw new IllegalArgumentException("保留范围必须大于或者等于0");
        }

        BigDecimal b = new BigDecimal(value);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 金额四舍五入
     * @param value 需要设置的值
     * @param scale  范围 0 表示取整 1表示保留1位小数  四舍五入
     * @return
     * create by sy.li12 李苏毅
     */
    public static BigDecimal setScale(BigDecimal value, Integer scale) {
        if (null == value) {
            return BigDecimal.valueOf(0);
        }
        if (null == scale || scale < 0) {
            throw new IllegalArgumentException("保留范围必须大于或者等于0");
        }
        return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * isNumeric:是否是数字. <br/>
     * @author 李苏毅
     * @email sy.li12@zuche.com
     * @date: 2016-12-23 下午3:25:15 <br>
     * @param str
     * @return
     * @since JDK 1.6
     * @Version V1.0
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private NumberUtil() {

    }
}

