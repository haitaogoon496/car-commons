package com.mljr.carrier;

import com.mljr.util.TimeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @description: 操作影像件组件
 * @Date : 2018/8/27 下午2:30
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Slf4j
@Component
public class CarrierComponent implements CarrierComposable{
    private final String LOG_TITLE = "影像件接口";
    private final String CALLER = "carrier-lyqc";//CARFINANCE-LYQC

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

    @Value("${carrier.publicKey:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC61Kw3LHVkQBSl0KhDV9KIK5JpEIE/N1eZeW1JVlNoayDDNYTWcSvywf6HgJRzeP6jrDsZHvZT5RrzP/JLoQHG1Tt4vvO/eEqWCII/326/NImvmugl/7AkilYTLD+r9kOnIevpGFvt+ZvTNdrwMIau+2shtofXToQOE1ddVVug2wIDAQAB}")
    private String publicKey;

    @Value("${carrier.env:prod}")
    private String env;

    @Override
    public byte[] getStream(String uuid) {
        /*try {
            log.info("{}下载下载文件,uuid={},env={},publicKey={}",LOG_TITLE,uuid,env,publicKey);
            CarrierClient client = new CarrierClient();
            CarrierDownloadRequest request = new CarrierDownloadRequest();
            request.setCaller(CALLER);
            request.setEnv(EnvEnum.getEnv(env));
            request.setUuid(uuid);
            request.setPublicKey(publicKey);
            Response<byte[]> response = client.downloadFile(request);
            if(response.isSuccess()){
                return response.getData();
            }else{
                log.error("{},uuid={},影像件下载失败",LOG_TITLE,uuid);
            }
        } catch (Exception e) {
            log.error("{},uuid={},影像件下载失败",LOG_TITLE,uuid,e);
        }*/
        return null;
    }

    @Override
    public String getUrl(String uuid) {
        /*try {
            log.info("{}获取影像件URL,uuid={},env={},publicKey={}",LOG_TITLE,uuid,env,publicKey);
            CarrierClient client = new CarrierClient();
            CarrierBatchRequest request = new CarrierBatchRequest();
            request.setCaller(CALLER);
            request.setEnv(EnvEnum.getEnv(env));
            request.setUuids(uuid);
            request.setPublicKey(publicKey);
            Response<Map<String, String>> mapResponse = client.queryImageUrlByUUIDs(request);
            if(mapResponse.isSuccess()){
                return mapResponse.getData().get(uuid);
            }else{
                log.error("{}uuid={}获取影像件URL失败,response:{}", LOG_TITLE, uuid, JSON.toJSON(mapResponse));
            }
        } catch (Exception e) {
            log.error("{},uuid={}获取影像件URLl失败",LOG_TITLE,uuid,e);
        }*/
        return null;
    }

    @Override
    public String downloadFromCarrierAndWriteShare(String fileName,String uuid) {
        String filePath = null;
        try {
            filePath = writeFileStream(fileName,getStream(uuid));
        } catch (Exception e) {
            log.error("{},uuid={},fileName={}写入共享盘失败",LOG_TITLE,uuid,fileName,e);
            return filePath;
        }
        log.info("{}写入共享盘成功,uuid={},env={},publicKey={}",LOG_TITLE,uuid,env,publicKey);
        return filePath;
    }

    @Override
    public String uploadStream(byte[] streams) {
        final String defaultName = "GPS资料_" + TimeTools.format(TimeTools.createNowTime(),"yyyyMMddHHmmss") + System.currentTimeMillis() + ".pdf";
        return uploadStream(streams,defaultName);
    }

    @Override
    public String uploadStream(byte[] streams,String fileName) {
        String imgKey = null;
        /*try {
            log.info("{}上传影像件-文件名称:{},env={},publicKey={}", LOG_TITLE, fileName, env, publicKey);
            CarrierClient client = new CarrierClient();
            CarrierUploadWithByteRequest request = new CarrierUploadWithByteRequest();
            request.setCaller(CALLER);
            request.setEnv(EnvEnum.getEnv(env));
            request.setPublicKey(publicKey);
            request.setFile(streams);
            request.setFileName(fileName);
            Response<String> response = client.uploadFile(request);
            if(response.getErrorCode() == 0){
                imgKey = response.getData();
            }else{
                log.info("{}上传影像件失败,env={},publicKey={},msg:{}",LOG_TITLE,env,publicKey,response.getErrorMsg());
            }
        } catch (Exception e) {
            log.error("{}上传影像件失败",LOG_TITLE,e);
        }*/
        return imgKey;
    }

    @Override
    public String writeShare(String fileName, byte[] streams) {
        String filePath = null;
        try {
            log.info("{}写入共享盘成功,env={},publicKey={}",LOG_TITLE,env,publicKey);
            filePath = writeFileStream(fileName,streams);
        } catch (Exception e) {
            log.error("{},fileName={}写入共享盘失败",LOG_TITLE,fileName,e);
        }finally {
            log.error("{},fileName={}写入共享盘成功",LOG_TITLE,fileName);
        }
        return filePath;
    }

    /**
     * 写文件
     * @param fileName 文件名称
     * @param streams 文件流
     * @return
     * @throws Exception
     */
    private String writeFileStream(String fileName, byte[] streams) throws Exception{
        String filePath = null;
        if(streams != null && streams.length > 0) {
            filePath = SHARE_DIR + fileName;
            File file = new File(SHARE_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            OutputStream out = new FileOutputStream(filePath);
            InputStream is = new ByteArrayInputStream(streams);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
            is.close();
            out.close();
        }
        return filePath;
    }
}
