package com.mljr.ding.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: actionCard类型的钉钉
 * @Date : 2019/3/21$ 11:14$
 * @Author : liht
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionCardRobotReq extends BaseDingRobotReq {
    private String msgType;
    private String title;
    private String text;
    private List<BtnInfoRobotReq> btnInfoRobotReqs;
    private String btnOrientation;
    private String hideAvatar;
    /**
     * 整体跳转时参数
     */
    private String singleTitle;
    /**
     * 整体跳转时的参数
     */
    private String singleURL;
}
