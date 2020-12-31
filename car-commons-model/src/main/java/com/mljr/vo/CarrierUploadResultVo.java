package com.mljr.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 影像件上传结果VO对象
 * @Date : 2018/8/27 下午6:44
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrierUploadResultVo implements Serializable {
    private static final long serialVersionUID = -9135917688649015769L;
    /**
     * 影像中心key
     */
    private String carrierKey;
    /**
     * share盘地址
     */
    private String sharePath;
}
