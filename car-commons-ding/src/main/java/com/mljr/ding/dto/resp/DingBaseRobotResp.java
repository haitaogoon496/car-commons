package com.mljr.ding.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: 响应Response实体类
 * @Date : 2018/8/5 下午4:06
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@AllArgsConstructor
public class DingBaseRobotResp extends DingBaseResp {

    Integer errcode = 0;
    String errmsg;
    Exception exception;

    public DingBaseRobotResp() {
    }

    public boolean isSuccess() {
        return super.isSuccess() && errcode == 0;
    }
}
