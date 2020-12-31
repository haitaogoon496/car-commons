package com.mljr.position;

import com.alibaba.fastjson.JSON;
import com.lyqc.base.entity.CaAppInfoEntity;
import com.lyqc.base.entity.CaAppInfoFeeEntity;
import com.lyqc.base.entity.CaAutoApprRuleEntity;
import com.lyqc.base.entity.CaAutoApprRulePropEntity;
import com.lyqc.base.entity.CaCarInfoEntity;
import com.lyqc.base.enums.AutoApprConstant.OpIdnEnum;
import com.lyqc.base.enums.AutoApprConstant.PropNameEnum;
import com.lyqc.base.enums.AutoApprConstant.PropValueTypeEnum;
import com.lyqc.util.BeanMapper;
import com.mljr.aviator.AviatorContext;
import com.mljr.aviator.AviatorExecutor;
import com.mljr.position.strategy.PositionStrategyHandle;
import com.mljr.position.strategy.PositionStrategyHandleFactory;
import com.mljr.position.strategy.PositionStrategyException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @description: 抽象置入执行器
 * @Date : 2018/11/23 下午3:40
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Slf4j
public abstract class AbstractPositionExecutor {
    /**
     * 申请单号
     */
    protected String appCode;
    /**
     * 上下文对象
     */
    protected PositionContext context;
    /**
     * 满足规则
     */
    protected CaAutoApprRuleEntity matchRule;
    /**
     * 标示某规则策略满足执行
     */
    protected boolean executeRule;
    /**
     * 存储内部因为异常导致的规则ID集合
     */
    protected Set<String> ignoreRuleIdSet = new HashSet<>(30);
    /**
     * 引擎执行参数环境
     */
    protected Map<String, Object> env = new HashMap<>();
    /**
     * 策略处理器
     */
    protected PositionStrategyHandle strategyHandle;
    /**
     * 构造函数
     * @param context
     */
    public AbstractPositionExecutor(PositionContext context) {
        this.context = context;
    }

    /**
     * 初始化处理
     */
    protected void prepare(){
        //申请单信息
        CaAppInfoEntity appInfo = context.getAppInfo();
        appCode = appInfo.getAppCode();
        strategyHandle = PositionStrategyHandleFactory.create(context.getStrategy());
        //车辆信息
        CaCarInfoEntity carInfo =  context.getCarInfo();
        //申请单费用信息
        CaAppInfoFeeEntity caAppInfoFee = context.getAppInfoFee();
        env.putAll(BeanMapper.objectToMap(appInfo));
        env.putAll(BeanMapper.objectToMap(carInfo));
        if(caAppInfoFee != null) {
            env.putAll(BeanMapper.objectToMap(caAppInfoFee));
        }
    }
    /**
     * 调用处理
     */
    protected void call() throws PositionException{
        PropNameEnum propNameEnum;
        OpIdnEnum opIdnEnum;
        PropValueTypeEnum valueTypeEnum;
        try {
            for(CaAutoApprRuleEntity ruleEntity : context.getRules()){
                List<CaAutoApprRulePropEntity> propList = ruleEntity.getProps();
                for(CaAutoApprRulePropEntity propEntity : propList){
                    try {
                        boolean currentMatch;
                        propNameEnum = PropNameEnum.getByName(propEntity.getPropCode());
                        opIdnEnum = OpIdnEnum.getByName(propEntity.getOpIdn());
                        valueTypeEnum =  PropValueTypeEnum.getByIndex(propEntity.getPropValueType());
                        if(PropNameEnum.EXPRESSION == propNameEnum){
                            currentMatch = access(propEntity.getPropValue());
                        }else{
                            currentMatch = OpSymbols.builder()
                                    .env(env).opIdnEnum(opIdnEnum).valueTypeEnum(valueTypeEnum)
                                    .propCode(propEntity.getPropCode()).propValue(propEntity.getPropValue())
                                    .build().access();
                        }
                        if(!currentMatch){
                            executeRule = false;
                            break;
                        }else{
                            executeRule = true;
                        }
                    } catch (Exception e) {
                        ignoreRuleIdSet.add(ruleEntity.getRuleId());
                        executeRule = false;
                        strategyHandle.handle(e,this,ruleEntity);
                        break;
                    }
                }
                if(executeRule){
                    matchRule = ruleEntity;
                    break;
                }else{
                    matchRule = null;
                }
            }
        }catch (PositionStrategyException e) {
            throw new PositionStrategyException(e);
        }catch (PositionException e) {
            log.error("置入执行异常",e);
            throw new PositionException(e);
        }catch (Exception e) {
            log.error("置入执行异常",e);
            throw new PositionException(e);
        }
    }
    /**
     * 校验公式是否通过
     * @return
     */
    protected boolean access(String expression){
        AviatorContext ctx = AviatorContext.builder().expression(expression).env(env).build();
        return AviatorExecutor.executeBoolean(ctx);
    }
    /**
     * 后置处理
     */
    protected void after(){
        if(executeRule){
            AccessResult accessResult = AccessResult.builder()
                    .ruleId(matchRule.getRuleId()).message(matchRule.getMsgTemplate()).access(executeRule).ignoreRuleIdSet(ignoreRuleIdSet)
                    .build();
            context.setResult(accessResult);
        }else{
            AccessResult accessResult = AccessResult.builder().access(executeRule).ignoreRuleIdSet(ignoreRuleIdSet).build();
            context.setResult(accessResult);
        }
        log.info("appCode={},executeRule={},matchRule={}",appCode,executeRule,JSON.toJSON(matchRule));
    }
    /**
     * 外部调用
     */
    public final void execute() throws PositionException{
        execute(env -> {});
    }
    /**
     * 外部调用，支持对公式变量进行设置
     * @param envMapper 公式变量设置
     * @throws PositionException
     */
    public final void execute(Consumer<Map<String, Object>> envMapper) throws PositionException{
        prepare();
        envMapper.accept(env);
        try {
            call();
        } catch (PositionStrategyException e) {
            after();
            throw new PositionStrategyException(e);
        }
        after();
    }
}
