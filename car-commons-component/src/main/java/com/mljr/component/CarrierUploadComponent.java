package com.mljr.component;

import com.alibaba.fastjson.JSON;
import com.lyqc.base.common.Result;
import com.lyqc.base.enums.RemoteEnum;
import com.mljr.annotation.LogMonitor;
import com.mljr.annotation.OvalValidator;
import com.mljr.carrier.CarrierComponent;
import com.mljr.file.FileUtil;
import com.mljr.util.TimeTools;
import com.mljr.vo.CarrierUploadResultVo;
import com.mljr.vo.FileUploadVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

/**
 * @description: 影像件上传处理器
 * @Date : 2018/8/27 下午6:38
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Slf4j
@Component
public class CarrierUploadComponent {
    private final String LOG_TITLE = "影像件上传";
    @Autowired
    private CarrierComponent carrierComponent;
    /**
     * =====================测试=====================
     * 直销：/share/liyun-zy/filedoc/approvl_pdf/
     * 渠道：/data/sp_filedoc/sp_approvl_pdf/
     * =====================生产=====================
     * 直销：/share/liyun-zy/filedoc/approvl_pdf/
     * 渠道：/share/liyun-zy/sp_filedoc/sp_approvl_pdf/
     */
    @Value("${carGps_app_pdf:/share/liyun-zy/filedoc/approvl_pdf/}")
    private String SHARE_DIR;

    /**
     * 3*1024*1024
     */
    @Value("${file.upload.maxFileSize:3145728}")
    private long MAX_FILE_SIZE;

    /**
     * 上传
     * @param fileUploadVo
     * @return
     */
    @LogMonitor("影像件上传")
    @OvalValidator("影像件上传")
    public Result<CarrierUploadResultVo> upload(FileUploadVo fileUploadVo){
        CarrierUploadResultVo resultVo = null;
        try {
            String appCode = fileUploadVo.getAppCode();
            long fileSize = fileUploadVo.getFileSize();
            InputStream inputStream = fileUploadVo.getInputStream();
            String fileSuffix = fileUploadVo.getFileSuffix();
            String delimiter = Optional.ofNullable(fileUploadVo.getDelimiter()).orElse("GPS资料");
            boolean limitFileSize = fileUploadVo.isLimitFileSize();
            if(limitFileSize && fileSize > MAX_FILE_SIZE){
                return Result.fail(resultVo, RemoteEnum.ERROR_IN_BUSINESS,"文件大小不能超过" + MAX_FILE_SIZE);
            }
            StringBuffer filePath = new StringBuffer(SHARE_DIR).append(appCode).append(delimiter).append(TimeTools.format4YYYYMMDDHHMISS(TimeTools.createNowTime())).append(fileSuffix);
            File destination = new File(filePath.toString());
            FileUtils.copyInputStreamToFile(inputStream,destination);
            byte[] streams = FileUtil.input2byte(inputStream);
            String sharePath = filePath.toString();
            String carrierKey = carrierComponent.uploadStream(streams);
            resultVo = CarrierUploadResultVo.builder().carrierKey(carrierKey).sharePath(sharePath).build();
        } catch (Exception e) {
            log.error("{},fileUploadVo={}异常",LOG_TITLE, JSON.toJSON(fileUploadVo),e);
        }
        return Result.suc(resultVo);
    }
}
