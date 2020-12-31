package com.mljr.ding.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 抽象响应Response实体类
 * @Date : 2018/8/5 下午4:06
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class DingBaseResp implements Serializable {

    String url;
    int httpCode;
    String httpMessage;

    public boolean isSuccess() {
        return httpCode == 200;
    }
}
