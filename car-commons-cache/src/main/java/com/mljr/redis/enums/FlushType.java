package com.mljr.redis.enums;


/**
 * @description: Redis操作类型
 * @Date : 2018/4/23 下午2:37
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public enum FlushType  {
    SET(1, "SET"),
    DEL(2, "DEL");
    FlushType(int index, String name) {
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
        for(FlushType e : FlushType.values()){
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
    public static FlushType getByIndex(int index){
        for(FlushType e : FlushType.values()){
            if(e.getIndex() == index){
                return e;
            }
        }
        return null;
    }
}
