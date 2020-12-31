package com.mljr.ding.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 钉钉@实体类
 * @Date : 2018/8/5 下午4:06
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class DingAt implements Serializable {

    Boolean isAtAll;//@所有人时:true,否则为:false
    List<String> atMobiles;// 被@人的手机号
}
