package com.mljr.ding.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 链接类型请求包体类
 * @Date : 2018/8/5 下午4:06
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@NoArgsConstructor
public class LinkDingRobotReq extends BaseDingRobotReq {

    String msgtype = "link";//(必填)消息类型，此时固定为
    String title;//(必填)首屏会话透出的展示内容
    String text;//(必填)markdown格式的消息
    String messageUrl;//(必填)点击消息跳转的URL
    String picUrl;//图片URL
}
