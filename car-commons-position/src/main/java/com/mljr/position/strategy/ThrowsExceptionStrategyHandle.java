package com.mljr.position.strategy;

import com.alibaba.fastjson.JSON;
import com.lyqc.base.entity.CaAutoApprRuleEntity;
import com.mljr.position.AbstractPositionExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * @description: 多个规则执行时，如果当前规则执行出现运行时异常，则抛出该异常
 * @Date : 2018/12/12 下午2:11
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Slf4j
public class ThrowsExceptionStrategyHandle implements PositionStrategyHandle{
    final String pattern = "规则[{0}]内部错误，规则名称={1}";
    @Override
    public void handle(Throwable throwable, AbstractPositionExecutor executor, CaAutoApprRuleEntity ruleEntity) throws PositionStrategyException {
        String message = MessageFormat.format(pattern,ruleEntity.getRuleId(),ruleEntity.getRuleName());
        log.error("{0},ruleEntity={1}",message, JSON.toJSON(ruleEntity),throwable);
        throw new PositionStrategyException(message,throwable);
    }
}
