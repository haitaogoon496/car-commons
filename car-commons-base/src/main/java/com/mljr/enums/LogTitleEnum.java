package com.mljr.enums;

import com.lyqc.base.enums.EnumValue;

/**
 * @description: 模块LogTitle枚举
 * @Date : 下午12:48 2018/2/5
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public enum LogTitleEnum implements EnumValue {

    UNKNOWN(-1,"未知模块"),

    /**
     * 通用
     */
    SY_ARG_CONTROL(1,"数据字典"),
    USER_LOGIN(10001,"用户登录"),
    CONFIG_PARAMS(22, "系统参数配置"),

    /**
     * 金融产品中心
     */
    GPS_RULE(1, "GPS规则配置"),
    RATE_RULE(2, "利率规则配置"),
    SER_FIN_RULE(3, "平台费规则配置"),
    SECOND_INSURANCE_RULE(4, "第二年保险费规则配置"),
    THIRD_INSURANCE_RULE(5, "第三年保险费规则配置"),
    ACCOUNT_RULE(6, "账户管理费规则配置"),
    LIFE_INSURANCE(7, "人身保险费规则配置"),
    PRODUCT_MANAGE(8, "车贷产品管理"),
    DEALER(9, "门店"),
    EXTEND_SAFE_RULE(10,"延保费规则配置"),
    PRODUCT(11,"车贷产品管理"),
    PRODUCT_RULE(12,"车贷产品组件"),
    PRODUCT_COMPONENT(13,"车贷产品规则"),
    PRODUCT_TYPE(14,"产品分类"),
    PRODUCT_CONTRACT(17,"产品对应合同"),
    CALC_MODEL(18, "计算公式配置"),
    CALC_MODEL_PARAMS(19, "产品计算模型参数配置"),
    CALC_LOG(20, "产品计算log"),
    GRAYSCALE_CONTROL(21, "灰度中心控制"),
    AUTO_APPR_RULE(23, "CA系统自动审批规则表"),
    AUTO_APPR_RULE_PROP(24, "CA系统自动审批规则属性表"),
    APPLY_DEALER(25, "管理所属门店"),
    TAG(26, "标签操作"),
    PD_RULE_GROUP(27, "规则组配置"),
    DEALER_FEE_RULE_SET(28, "门店费用规则关联设定"),
    PD_FEE_PRODUCT(29, "续保押金关联产品配置"),

    FUND(30, "资金方"),
    PD_FUND_RULE(31, "资金方准入规则"),
    PD_FUND_RULE_PROP(32, "资金方准入规则属性"),
    POST_POSITION_RULE(33, "后置规则"),
    POST_POSITION_RULE_DETAUL(34, "后置规则属性"),
    CALC_FORMULA_PARAMS(35, "产品公式合法参数配置"),
    DEALER_COMPANY(36, "门店单位"),


    /**
     * GPS模块
     */
    GpsCompositeQuery(10000, "GPS综合查询"),
    GPS_APPLY_FORM(10003, "GPS申请单"),
    GPS_APPROVAL_BACK(10004, "GPS审批退回操作"),
    GPS_DEVICE(10005, "GPS设备操作"),
    GPS_DEVICE_MODIFY(10006, "GPS设备更新"),

    GPS_INSTALL_INFO(10007, "GPS安装信息"),
    CA_APP_INFO(10008, "申请单"),
    CA_CAR_INFO(10009, "车辆信息"),
    GPS_APPROVE_RESULT(10010, "GPS设备认证结果"),
    GPS_DATA_INFO(10011,"GPS设备信息"),
    GPS_CONTRACT(10012,"GPS安装联系人"),
    GPS_DEALER(10013,"GPS供应商"),
    GPS_APPROVE(10014,"GPS人工审批")


    ;
    LogTitleEnum(int index, String name) {
        this.index = index;
        this.name = name;
    }

    private int index;
    private String name;

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 根据索引获取名称
     * @param index 索引
     * @return
     */
    public static String getNameByIndex(int index){
        for(LogTitleEnum e : LogTitleEnum.values()){
            if(e.getIndex() == index){
                return e.getName();
            }
        }
        return null;
    }

    /**
     * 根据索引获取枚举对象
     * @param index 索引
     * @return
     */
    public static LogTitleEnum getByIndex(int index){
        for(LogTitleEnum e : LogTitleEnum.values()){
            if(e.getIndex() == index){
                return e;
            }
        }
        return null;
    }
}
