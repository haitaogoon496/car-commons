package com.mljr.position.strategy;

/**
 * @description: 置入规则策略处理接口工厂类
 * @Date : 2018/12/11 下午4:08
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public final class PositionStrategyHandleFactory {
    /**
     * 工厂方法
     * @param strategy
     * @return
     */
    public static PositionStrategyHandle create(PositionStrategy strategy){
        switch (strategy){
            case STRICT_EXECUTE:
                return new StrictExecuteStrategyHandle();
            case IGNORE_EXCEPTION:
                return new IgnoreExceptionStrategyHandle();
            case THROWS_EXCEPTION:
                return new ThrowsExceptionStrategyHandle();
            default:
                throw new IllegalArgumentException("UNKNOWN strategy="+strategy.toString());
        }
    }
}
