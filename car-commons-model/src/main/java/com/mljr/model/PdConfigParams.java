package com.mljr.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;

/**
 * @description: 产品全局参数
 * @Date : 2018/4/11 下午4:59
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdConfigParams implements Serializable{
    private static final long serialVersionUID = 2974487365822467730L;
    @ApiModelProperty(value = "id")
    private Integer id;
    @NotNull(message = "paramKey不能为空")
    @ApiModelProperty(value = "参数key")
    private String paramKey;
    @NotNull(message = "paramValue不能为空")
    @ApiModelProperty(value = "参数值")
    private String paramValue;
    @NotNull(message = "paramDesc不能为空")
    @ApiModelProperty(value = "参数描述")
    private String paramDesc;
    @ApiModelProperty(value = "状态(1:启用 0:暂停)")
    private Integer status;
}