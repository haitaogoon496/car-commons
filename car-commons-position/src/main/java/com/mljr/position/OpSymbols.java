package com.mljr.position;

import com.lyqc.base.enums.AutoApprConstant.OpIdnEnum;
import com.lyqc.base.enums.AutoApprConstant.PropValueTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 操作符工具类
 * @Date : 2018/12/3 下午6:07
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class OpSymbols {
    /**
     * 数值类型，置为空，则取该默认值0
     */
    final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;
    /**
     * 字符串类型为空时默认值
     */
    final String DEFAULT_PROP_VALUE = "";
    /**
     * 数字类型中处理字符串的操作符
     */
    final List<OpIdnEnum> DECIMAL_FOR_CHAR_OPS = Arrays.asList(OpIdnEnum.IN,OpIdnEnum.NOT_IN);
    /**
     * 操作符
     */
    OpIdnEnum opIdnEnum;
    /**
     * 属性类型
     */
    PropValueTypeEnum valueTypeEnum;
    /**
     * 属性代码
     */
    String propCode;
    /**
     * 属性值
     */
    String propValue;
    /**
     * 表达式依赖参数变量
     */
    Map<String,Object> env;
    /**
     * 校验
     * @return
     */
    public boolean access(){
        assertNotNull(opIdnEnum,"opIdnEnum不能为空");
        assertNotNull(valueTypeEnum,"valueTypeEnum不能为空");
        assertNotNull(propCode,"propCode不能为空");
        assertNotNull(propValue,"propValue不能为空");
        if(PropValueTypeEnum.FLOAT == valueTypeEnum || PropValueTypeEnum.INTEGER == valueTypeEnum){
            String targetValue = Optional.ofNullable(env.get(propCode)).orElse(DEFAULT_VALUE.toString()).toString();
            if(DECIMAL_FOR_CHAR_OPS.contains(opIdnEnum)){
                switch (opIdnEnum){
                    case IN:
                        return in(propValue, targetValue);
                    case NOT_IN:
                        return notIn(propValue, targetValue);
                }
            }else{
                BigDecimal propValueDecimal = new BigDecimal(Optional.ofNullable(propValue).orElse(DEFAULT_VALUE.toString()).toString());
                BigDecimal targetValueDecimal = new BigDecimal(targetValue.toString());
                switch (opIdnEnum){
                    case NE:
                        return ne(propValueDecimal, targetValueDecimal);
                    case EQUAL:
                        return eq(propValueDecimal, targetValueDecimal);
                    case GT:
                        return gt(propValueDecimal, targetValueDecimal);
                    case GE:
                        return ge(propValueDecimal, targetValueDecimal);
                    case LT:
                        return lt(propValueDecimal, targetValueDecimal);
                    case LE:
                        return le(propValueDecimal, targetValueDecimal);
                }
            }
        }
        if(PropValueTypeEnum.STRING == valueTypeEnum){
            String targetValue = Optional.ofNullable(env.get(propCode)).orElse(DEFAULT_PROP_VALUE).toString();
            switch (opIdnEnum){
                case IN:
                    return in(propValue, targetValue);
                case NOT_IN:
                    return notIn(propValue, targetValue);
                case NE:
                    return ne(propValue, targetValue);
                case EQUAL:
                    return eq(propValue, targetValue);
            }
        }
        return false;
    }

    /**
     * 判断 targetValue 以,分割德尔字符串包含 propValue
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean in(String propValue,String targetValue){
        return Arrays.asList(propValue.split(",")).contains(targetValue);
    }
    /**
     * 判断 targetValue 以,分割德尔字符串不包含 propValue
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean notIn(String propValue,String targetValue){
        return !in(propValue,targetValue);
    }

    /**
     * 判断两个字符串相等
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean eq(String propValue,String targetValue){
        return propValue.equals(targetValue);
    }
    /**
     * 判断两个数值相等
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean eq(BigDecimal propValue, BigDecimal targetValue){
        return propValue.compareTo(targetValue) == 0;
    }
    /**
     * 判断两个字符串不等
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean ne(String propValue,String targetValue){
        return !eq(propValue,targetValue);
    }
    /**
     * 判断两个数值不等
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean ne(BigDecimal propValue, BigDecimal targetValue){
        return !eq(propValue,targetValue);
    }

    /**
     * 判断 propValue <= targetValue
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean ge(BigDecimal propValue, BigDecimal targetValue){
        return propValue.compareTo(targetValue) <= 0;
    }
    /**
     * 判断 propValue >= targetValue
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean le(BigDecimal propValue, BigDecimal targetValue){
        return propValue.compareTo(targetValue) >= 0;
    }
    /**
     * 判断 targetValue > propValue
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean gt(BigDecimal propValue, BigDecimal targetValue){
        return propValue.compareTo(targetValue) < 0;
    }

    /**
     * 判断 propValue > targetValue
     * @param propValue
     * @param targetValue
     * @return
     */
    boolean lt(BigDecimal propValue, BigDecimal targetValue){
        return propValue.compareTo(targetValue) > 0;
    }

    /**
     * 非空断言
     * @param object
     * @param message
     */
    void assertNotNull(Object object,String message){
        if(null == object){
            throw new NullPointerException(message);
        }
    }

    /**
     * 判断字符串是否有值
     * @param value
     * @return
     */
    boolean hasText(String value){
        return null != value && !"".equals(value) && value.length() > 0;
    }
}
