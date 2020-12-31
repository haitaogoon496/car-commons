package com.mljr.position.strategy;

import com.lyqc.base.entity.CaAutoApprRuleEntity;
import com.mljr.position.AbstractPositionExecutor;

/**
 * @description: 置入规则策略处理接口
 * @Date : 2018/12/10 下午5:00
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface PositionStrategyHandle {
    /**
     * 处理
     * @param throwable 异常
     * @param executor 执行器对象
     * @param ruleEntity 规则实体对象
     */
    void handle(Throwable throwable, AbstractPositionExecutor executor, CaAutoApprRuleEntity ruleEntity) throws PositionStrategyException;
}
