package com.mljr.redis.enums;

/**
 * @description: Redis key前缀类型
 * @Date : 2018/4/23 下午2:37
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public enum KeyPrefix {
    CAR(0, "car"),
    LYQC(1, "lyqc"),
    CAR_PRODUCT(2, "carProduct"),
    LYQC_SEIG(3, "lyqcSeig"),
    CAR_GPS(4, "carGps"),
    CAR_YULV(5, "carYULV"),
    CAR_HADES(6, "carHades"),
    CAR_DHARMA(7, "carDharma"),
    CAR_TRANSFER(8, "carTransfer"),
    TRANSFER_WEB(9, "transferWeb");
    KeyPrefix(int index, String name) {
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
        for(KeyPrefix e : KeyPrefix.values()){
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
    public static KeyPrefix getByIndex(int index){
        for(KeyPrefix e : KeyPrefix.values()){
            if(e.getIndex() == index){
                return e;
            }
        }
        return null;
    }
}
