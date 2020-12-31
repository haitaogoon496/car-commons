package com.mljr.mapper;

import com.mljr.model.PdConfigParams;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 产品全局参数Mapper对象
 * @Date : 2018/4/11 下午5:01
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface PdConfigParamsMapper{
    /**
     * 查询所有数据刷新Redis
     * @return
     */
    @Select("select id,param_key as paramKey, param_value as paramValue,param_desc as paramDesc,status from pd_config_params")
    List<PdConfigParams> queryListForFlushRedis();
    /**
     * 根据 paramKey 查询
     * @param paramKey
     * @return PdConfigParams
     */
    @Select("select id,param_key as paramKey, param_value as paramValue,param_desc as paramDesc,status from pd_config_params where param_key = #{paramKey}")
    PdConfigParams queryByParamKey(@NotNull @Param("paramKey") String paramKey);

    /**
     * 保存 pd_config_params，如果存在更新 param_value
     * @param pdConfigParams
     * @return
     */
    @Insert("INSERT INTO `pd_config_params` (`param_key`, `param_value`, `param_desc`, `status`) VALUES (#{paramKey,jdbcType=VARCHAR}, #{paramValue,jdbcType=VARCHAR}, #{paramDesc,jdbcType=VARCHAR}, #{status,jdbcType=TINYINT}) ON duplicate KEY UPDATE param_value = #{paramValue,jdbcType=VARCHAR}")
    int savePdConfigParams(PdConfigParams pdConfigParams);
}