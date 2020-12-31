package com.mljr.position.strategy;

import com.alibaba.fastjson.JSON;
import com.lyqc.base.entity.CaAutoApprRuleEntity;
import com.mljr.position.AbstractPositionExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * @description: 多个规则执行时，如果当前规则执行出现运行时异常，则忽略继续执行
 * @Date : 2018/12/12 下午2:11
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Slf4j
public class IgnoreExceptionStrategyHandle implements PositionStrategyHandle{
    @Override
    public void handle(Throwable throwable, AbstractPositionExecutor executor, CaAutoApprRuleEntity ruleEntity) {
        log.warn("规则支持执行警告，规则将忽略继续执行,ruleEntity={0}",JSON.toJSON(ruleEntity),throwable);
    }
}
