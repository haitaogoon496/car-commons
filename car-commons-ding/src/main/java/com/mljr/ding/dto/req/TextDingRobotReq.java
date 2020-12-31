package com.mljr.ding.dto.req;

import com.mljr.ding.dto.DingAt;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: Text类型请求包体类
 * @Date : 2018/8/5 下午4:06
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@NoArgsConstructor
public class TextDingRobotReq extends BaseDingRobotReq {

    String msgtype = "text";//(必填)消息类型，此时固定为:text
    String content;//(必填)消息内容
    DingAt at;
}
