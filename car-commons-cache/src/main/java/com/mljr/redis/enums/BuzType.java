package com.mljr.redis.enums;

/**
 * @description: Redis业务类型枚举，用于区别生成Redis key
 * @Date : 2018/4/23 下午2:37
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public enum BuzType {
    DEFAULT(10000, "缺省默认"),
    PD_CONFIG_PARAMS(10001, "产品全局参数");
    BuzType(int index, String name) {
        this.index = index;
        this.name = name;
    }
    /**
     * 索引
     */
    private int index;
    /**
     * 名称
     */
    private String name;

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }


    /**
     * 根据索引获取名称
     * @param index 索引
     * @return
     */
    public static String getNameByIndex(int index){
        for(BuzType e : BuzType.values()){
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
    public static BuzType getByIndex(int index){
        for(BuzType e : BuzType.values()){
            if(e.getIndex() == index){
                return e;
            }
        }
        return null;
    }
}
