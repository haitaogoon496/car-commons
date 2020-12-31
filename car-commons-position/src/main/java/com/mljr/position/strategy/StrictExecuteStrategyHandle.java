package com.mljr.position.strategy;

import com.lyqc.base.entity.CaAutoApprRuleEntity;
import com.mljr.position.AbstractPositionExecutor;

/**
 * @description: 严谨执行所有规则
 * @Date : 2018/12/12 下午2:11
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class StrictExecuteStrategyHandle extends ThrowsExceptionStrategyHandle implements PositionStrategyHandle{
    @Override
    public void handle(Throwable throwable, AbstractPositionExecutor executor, CaAutoApprRuleEntity ruleEntity) throws PositionStrategyException {
        super.handle(throwable,executor,ruleEntity);
    }
}
