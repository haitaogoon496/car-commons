package com.mljr.position;

import com.lyqc.base.entity.CaAppInfoEntity;
import com.lyqc.base.entity.CaAppInfoFeeEntity;
import com.lyqc.base.entity.CaAutoApprRuleEntity;
import com.lyqc.base.entity.CaCarInfoEntity;
import com.mljr.position.strategy.PositionStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 置入上线文对象
 * （封装前置或后置引擎所需要的上线文对象依赖）
 * @Date : 2018/12/3 下午3:11
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionContext implements Serializable{
    private static final long serialVersionUID = -3356912462840000459L;
    /**
     * 规则执行策略
     */
    protected PositionStrategy strategy = PositionStrategy.IGNORE_EXCEPTION;
    /**
     * 贷款申请单信息
     */
    protected CaAppInfoEntity appInfo;
    /**
     * 贷款申请单费用信息
     */
    protected CaAppInfoFeeEntity appInfoFee;
    /**
     * 贷款申请单车辆信息
     */
    protected CaCarInfoEntity carInfo;
    /**
     * 置入规则
     */
    protected List<CaAutoApprRuleEntity> rules;
    /**
     * 返回规则策略结果
     */
    protected AccessResult result;
}
