package com.mljr.ding.dto.builder;

import com.mljr.ding.dto.req.MarkdownDingRobotReq;
import com.mljr.util.TimeTools;

import java.util.Optional;

/**
 * @description: MarkdownDingRobotReqBuilder
 * 提供了基于Markdown语法的内容处理
 * @Date : 2019/1/23 下午6:21
 * @Author : 石冬冬-Seig Heil
 */
public class MarkdownDingRobotReqBuilder {
    String title;
    String summary;
    String content;
    String applicationName;
    public MarkdownDingRobotReqBuilder title(String title){
        this.title = title;
        return this;
    }
    public MarkdownDingRobotReqBuilder summary(String summary){
        this.summary = summary;
        return this;
    }
    public MarkdownDingRobotReqBuilder content(String content){
        this.content = content;
        return this;
    }
    public MarkdownDingRobotReqBuilder applicationName(String applicationName){
        this.applicationName = applicationName;
        return this;
    }
    public MarkdownDingRobotReq build(){
        MarkdownDingRobotReq robotReq = new MarkdownDingRobotReq();
        robotReq.setTitle(this.title);
        StringBuffer content = new StringBuffer("**").append(this.title).append("**");
        content.append("\n\n **所属应用**：").append(Optional.ofNullable(this.applicationName).orElse("UNKNOWN"));
        content.append("\n\n **请求时间**：").append(TimeTools.format4YYYYMMDDHHMISS(TimeTools.createNowTime()));
        content.append("\n\n **报警内容**：\n\n > ").append(this.content);
        robotReq.setText(content.toString());
        return robotReq;
    }

    @Override
    public String toString() {
        return "MarkdownDingRobotReqBuilder{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", applicationName='" + applicationName + '\'' +
                '}';
    }

    public static void main(String[] args) {
        new MarkdownDingRobotReqBuilder().build();
    }
}
