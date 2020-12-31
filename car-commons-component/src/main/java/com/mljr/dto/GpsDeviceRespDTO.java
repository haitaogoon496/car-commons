package com.mljr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: GPS实时校验DTO
 * @Date : 2019/1/28 下午5:30
 * @Author : 石冬冬-Seig Heil
 */
@Data
public class GpsDeviceRespDTO<Ctx> implements Serializable{
    private static final long serialVersionUID = 3715275321071645841L;
    /**
     * 请求结果code
     */
    private Integer code;
    /**
     * 业务信息
     */
    private String message;
    /**
     * 调用Body
     */
    private Ctx context;
}