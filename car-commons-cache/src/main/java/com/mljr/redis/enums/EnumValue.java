package com.mljr.redis.enums;

/**
 * @description: 枚举接口，一切枚举应从通用扩展宜实现此接口
 * @Date : 2017/9/19 下午6:22
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface EnumValue {

    /**
     * 获取枚举索引
     * @return
     */
    int getIndex();

    /**
     * 获取枚举名称
     * @return
     */
    String getName();
}

