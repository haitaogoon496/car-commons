package com.mljr.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: GPS实时校验请求TokenDTO
 * @Date : 2019/1/28 下午5:33
 * @Author : 石冬冬-Seig Heil
 */
@Data
public class GpsDeviceTokenDTO implements Serializable{
    private static final long serialVersionUID = -5370658044108979181L;

    private String uuid;

    private String token;

    private Long expireTime;

    private Long createTime;

    private Long updateTime;
}
