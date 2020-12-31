package com.mljr.position;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 抽象置入执行器
 * @Date : 2018/11/23 下午3:40
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Slf4j
public class PrePositionExecutor extends AbstractPositionExecutor {
    /**
     * 构造函数
     *
     * @param context
     */
    public PrePositionExecutor(PositionContext context) {
        super(context);
    }
}
