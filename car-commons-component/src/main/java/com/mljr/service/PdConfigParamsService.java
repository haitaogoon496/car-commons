package com.mljr.service;


import com.mljr.model.PdConfigParams;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 产品全局参数Service
 * @Date : 下午5:05 2018/4/11
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
public interface PdConfigParamsService{
    /**
     * 查询所有数据刷新Redis
     * @return
     */
    List<PdConfigParams> queryListForFlushRedis();
    /**
     * 根据 paramKey 查询
     * @param paramKey
     * @return PdConfigParams
     */
    PdConfigParams queryByParamKey(@NotNull String paramKey);

    /**
     * 保存 pd_config_params
     * @param pdConfigParams
     * @return
     */
    int savePdConfigParams(PdConfigParams pdConfigParams);
}
