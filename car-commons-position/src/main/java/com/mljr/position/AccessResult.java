package com.mljr.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @description: 验证结果
 * @Date : 2018/12/5 下午3:24
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AccessResult<T> {
    private static final int SUCCESS = 1;
    private static final int FAILURE = 0;
    /**
     * 是否满足规则策略
     */
    protected boolean access;
    /**
     * code
     */
    protected int code = SUCCESS;
    /**
     * 满足规则ID
     */
    protected String ruleId;
    /**
     * 满足规则提示
     */
    protected String message;
    /**
     * 存储内部因为异常导致的规则ID集合
     */
    protected Set<String> ignoreRuleIdSet;
    /**
     * 数据（扩展使用）
     */
    protected T data;
    /**
     * 异常处理
     * @param message 异常信息
     * @return
     */
    public static AccessResult exception(String message){
        return AccessResult.builder().code(FAILURE).message(message).build();
    }
}
