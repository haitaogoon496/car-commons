package com.mljr.service.impl;

import com.mljr.mapper.PdConfigParamsMapper;
import com.mljr.model.PdConfigParams;
import com.mljr.service.PdConfigParamsService;
import com.mljr.util.CollectionsTools;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 产品全局参数Service
 * @Date : 下午5:06 2018/4/11
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
@Service
public class PdConfigParamsServiceImpl implements PdConfigParamsService {
    @Autowired
    private PdConfigParamsMapper pdConfigParamsMapper;

    @Override
    public List<PdConfigParams> queryListForFlushRedis() {
        return pdConfigParamsMapper.queryListForFlushRedis();
    }

    @Override
    public PdConfigParams queryByParamKey(String paramKey) {
        return pdConfigParamsMapper.queryByParamKey(paramKey);
    }

    @Override
    public int savePdConfigParams(PdConfigParams pdConfigParams) {
        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(pdConfigParams);
        if (CollectionsTools.isNotEmpty(violations)) {
            return 0;
        }
        return pdConfigParamsMapper.savePdConfigParams(pdConfigParams);
    }

}
