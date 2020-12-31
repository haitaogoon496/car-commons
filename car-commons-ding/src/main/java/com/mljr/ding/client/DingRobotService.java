package com.mljr.ding.client;


import com.mljr.ding.dto.req.ActionCardRobotReq;
import com.mljr.ding.dto.resp.DingBaseRobotResp;
import com.mljr.ding.dto.req.LinkDingRobotReq;
import com.mljr.ding.dto.req.MarkdownDingRobotReq;
import com.mljr.ding.dto.req.TextDingRobotReq;

/**
 * @description: 钉钉机器人接口
 * @Date : 2018/8/5 下午4:04
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface DingRobotService {

    /**
     * 发送text类型
     */
    DingBaseRobotResp sendText(String robotToken, TextDingRobotReq req);

    /**
     * 发送link类型
     */
    DingBaseRobotResp sendLink(String robotToken, LinkDingRobotReq req);

    /**
     * 发送markdown类型
     */
    DingBaseRobotResp sendMarkdown(String robotToken, MarkdownDingRobotReq req);

    /**
     * 发送actionCard类型
     * @param robotToken
     * @param robotReq
     * @return
     */
    DingBaseRobotResp sendActionCard(String robotToken, ActionCardRobotReq robotReq);
}