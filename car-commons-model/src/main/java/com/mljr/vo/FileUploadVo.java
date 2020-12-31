package com.mljr.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.oval.constraint.MinSize;
import net.sf.oval.constraint.NotEmpty;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @description: 文件上传VO对象
 * @Date : 2018/8/27 下午6:41
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadVo implements Serializable{
    private static final long serialVersionUID = -8051809594669608100L;
    /**
     * appCode
     */
    @ApiModelProperty(name="appCode",value="appCode",dataType="String")
    @NotEmpty(message = "appCode非空")
    private String appCode;
    /**
     * delimiter
     */
    @ApiModelProperty(name="delimiter",value="存储文件连接符",dataType="String")
    private String delimiter;
    /**
     * 原文件名称
     */
    @ApiModelProperty(name="originalFileName",value="原文件名称",dataType="String")
    @NotEmpty(message = "originalFileName非空")
    private String originalFileName;
    /**
     * 文件后缀类型
     */
    @ApiModelProperty(name="fileSuffix",value="文件后缀类型",dataType="String")
    @NotEmpty(message = "fileSuffix非空")
    private String fileSuffix;
    /**
     * 文件大小
     */
    @ApiModelProperty(name="fileSize",value="文件大小",dataType="long")
    @MinSize(value = 0,message = "文件大小不能小于0")
    private long fileSize;
    /**
     * 文件流
     */
    @ApiModelProperty(name="inputStream",value="文件流",dataType="InputStream")
    private InputStream inputStream;
    /**
     * 是否控制文件大小
     */
    @ApiModelProperty(name="limitFileSize",value="是否控制文件大小",dataType="boolean")
    private boolean limitFileSize;
}
