package com.mljr.position.strategy;

/**
 * @description: 置入执行策略枚举
 * @Date : 2018/12/5 下午3:48
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public enum PositionStrategy {
    /**
     * 严谨执行所有规则
     */
    STRICT_EXECUTE,
    /**
     * 多个规则执行时，如果当前规则执行出现运行时异常，则忽略继续执行
     */
    IGNORE_EXCEPTION,
    /**
     * 多个规则执行时，如果当前规则执行出现运行时异常，则抛出该异常
     */
    THROWS_EXCEPTION
}