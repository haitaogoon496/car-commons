package com.mljr.ding.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mljr.ding.dto.req.ActionCardRobotReq;
import com.mljr.ding.dto.req.LinkDingRobotReq;
import com.mljr.ding.dto.req.MarkdownDingRobotReq;
import com.mljr.ding.dto.req.TextDingRobotReq;
import com.mljr.ding.dto.resp.DingBaseRobotResp;
import com.mljr.util.CollectionsTools;
import com.mljr.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 钉钉机器人客户端类
 * @Date : 2018/8/5 下午4:05
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Component
@Slf4j
public class DingRobotClient implements DingRobotService {

    final String robotBaseUrl = "https://oapi.dingtalk.com/robot/send?access_token=";

    /**
     * 发送text类型
     * <code>
     *     TextDingRobotReq robotReq = new TextDingRobotReq();
     *     String content = "기다리게 해서 죄송합니다";
     *     robotReq.setContent(content);
     *     DingRobotService dingRobotService = new DingRobotClient();
     *     DingBaseRobotResp resp = dingRobotService.sendText(DingAuth.TOKEN, robotReq);
     * </code>
     * @param robotToken {@link com.mljr.ding.auth.DingAuth#TOKEN}
     * @param req
     */
    @Override
    public DingBaseRobotResp sendText(String robotToken, TextDingRobotReq req) {
        Map<String, String> text = new HashMap<>();
        text.put("content", req.getContent());

        Map<String, Object> map = new HashMap<>(3);
        map.put("msgtype", req.getMsgtype());
        map.put("text", text);
        map.put("at", req.getAt());

        return request(robotToken, map);
    }

    private DingBaseRobotResp request(String robotToken, Map<String, Object> req) {
        String url = robotBaseUrl + robotToken;
        DingBaseRobotResp resp = new DingBaseRobotResp();
        resp.setUrl(url);
        try {
            String bodyString = JSON.toJSONString(req);
            log.info("dingRobot req:{} body:{}", url, bodyString);
            String respBody = OkHttpUtil.post(bodyString,url);
            JSONObject map = JSON.parseObject(respBody);
            resp.setErrcode(map.getInteger("errcode"));
            resp.setErrmsg(map.getString("errmsg"));
            log.info("dingRobot resp: {}", respBody);
        } catch (Exception e) {
            resp.setException(e);
        }
        return resp;
    }


    /**
     * 发送link类型
     * <code>
     *    LinkDingRobotReq robotReq = new LinkDingRobotReq();
     *    String content = "La capital de nuestro país es Beijing. ";
     *      robotReq.setTitle("Notice");
     *      robotReq.setText(content);
     *      robotReq.setMessageUrl("http://blog.sina.com.cn/s/blog_14b88cad50102x97b.html");
     *      robotReq.setPicUrl("http://s7.sinaimg.cn/large/0064qwnjzy78VDhJwKab6&690");
     *     DingBaseRobotResp resp = dingRobotService.sendText(DingAuth.TOKEN, robotReq);
     * </code>
     * @param robotToken {@link com.mljr.ding.auth.DingAuth#TOKEN}
     * @param robotToken
     * @param req
     */
    @Override
    public DingBaseRobotResp sendLink(String robotToken, LinkDingRobotReq req) {
        Map<String, String> link = new HashMap<>();
        link.put("title", req.getTitle());
        link.put("text", req.getText());
        link.put("messageUrl", req.getMessageUrl());
        link.put("picUrl", req.getPicUrl());

        Map<String, Object> map = new HashMap<>();
        map.put("msgtype", req.getMsgtype());
        map.put("link", link);

        return request(robotToken, map);
    }

    /**
     * 发送markdown类型
     * <code>
     *     MarkdownDingRobotReq robotReq = new MarkdownDingRobotReq();
     *     String content = "La capital de nuestro país es Beijing. ";
     *     robotReq.setTitle("Notice");
     *     robotReq.setText(content);
     *     DingBaseRobotResp resp = dingRobotService.sendText(DingAuth.TOKEN, robotReq);
     * </code>
     * @param robotToken {@link com.mljr.ding.auth.DingAuth#TOKEN}
     * @param robotToken
     * @param req
     */
    @Override
    public DingBaseRobotResp sendMarkdown(String robotToken, MarkdownDingRobotReq req) {
        Map<String, String> markdown = new HashMap<>();
        markdown.put("title", req.getTitle());
        markdown.put("text", req.getText());

        Map<String, Object> map = new HashMap<>();
        map.put("msgtype", req.getMsgtype());
        map.put("markdown", markdown);
        map.put("at", req.getAt());

        return request(robotToken, map);
    }

    /**
     * 发送actionCard类型
     * @param robotToken
     * @param robotReq
     * @return
     */
    @Override
    public DingBaseRobotResp sendActionCard(String robotToken, ActionCardRobotReq robotReq) {
        Map<String, Object> actionCard = new HashMap<>();
        actionCard.put("title", robotReq.getTitle());
        actionCard.put("text", robotReq.getText());
        if (CollectionsTools.isNotEmpty(robotReq.getBtnInfoRobotReqs())) {
            actionCard.put("btns", robotReq.getBtnInfoRobotReqs());
        }
        if (!StringUtils.isEmpty(robotReq.getSingleTitle())) {
            actionCard.put("singleTitle", robotReq.getSingleTitle());
        }
        if (!StringUtils.isEmpty(robotReq.getSingleURL())) {
            actionCard.put("singleURL", robotReq.getSingleURL());
        }
        actionCard.put("hideAvatar", robotReq.getHideAvatar());
        actionCard.put("btnOrientation", robotReq.getBtnOrientation());

        Map<String, Object> map = new HashMap<>();
        map.put("msgtype", robotReq.getMsgType());
        map.put("actionCard", actionCard);

        return request(robotToken, map);
    }
}
