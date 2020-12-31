package com.mljr.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.lyqc.base.common.Result;
import com.lyqc.base.enums.RemoteEnum;
import com.lyqc.gpsprovider.enums.CarGpsConstant;
import com.lyqc.gpsprovider.enums.GpsDeviceAgencyType;
import com.lyqc.gpsprovider.re.GpsDeviceInfoRe;
import com.mljr.ding.auth.DingAuth;
import com.mljr.ding.client.DingRobotService;
import com.mljr.ding.dto.req.MarkdownDingRobotReq;
import com.mljr.dto.GpsDeviceRespDTO;
import com.mljr.dto.GpsDeviceTokenDTO;
import com.mljr.exception.GpsDeviceException;
import com.mljr.redis.service.RedisService;
import com.mljr.util.CollectionsTools;
import com.mljr.util.OkHttpUtil;
import com.mljr.util.StringTools;
import com.mljr.util.TimeTools;
import com.mljr.vo.GpsDeviceInfo;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @description: 根据gps_ids获取gps信息
 * @Date : 2018/6/15 14:21
 * @Author : 樊康康-(kangkang.fan@mljr.com)
 */
@Component
public class GpsDeviceInfoComponent {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final String LOG_TITLE = "大数据GPS设备商认证";
    private final String MONITOR_LOG_TITLE = "大数据GPS设备商认证";
    /**
     * The default initial capacity - MUST be a power of two.
     */
    private final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    /**
     * GPS设备号钉钉报警前缀
     */
    public final String REDIS_DING_PREFIX = "CarGpsDingTalk:";
    /**
     * GPS设备号钉钉报警Redis时效 一天时间
     */
    private final long REDIS_DING_EXPIRE = 60*60*24;
    /**
     * GPS设备号钉钉报警 最大阈值，超过次数才报警，为了降低报警
     */
    private final long REDIS_DING_THRESHOLD = 2;
    /**
     * mock 正常的设备号，以"ZZ"开头
     */
    private final static String NORMAL_PREFIX = "ZS";
    /**
     * mock 异常的设备号，以"YS"开头
     */
    private final static String ABNORMAL_PREFIX = "YS";
    /**
     * mock 正常设备号
     */
    private final static String NORMAL_GPS = "1";
    /**
     * mock 非正常设备号
     */
    private final static String ABNORMAL_GPS = "2";
    /**
     * 正常状态码
     */
    private final  static String[] NORMAL_STATUS = new String[] { "NORMAL" };
    /**
     * 非正常状态
     */
    private final  static String[] ABNORMAL_STATUS = new String[] { "NO_REAL_INFO" };
    private final  static String[] UN_NORMAL_STATUS = new String[] { "NO_INIT","NO_TRACK","NO_TRACK","NO_SPEED","BLACK_AREA","NO_REAL_INFO","DIFF_CITY","INVALID_GPSID","OTHER" };

    public final static Integer STATUS_YES = 1;
    public final static Integer STATUS_NO = 0;
    /**
     * 获取GPS设备商认证接口
     */
    @Value("${gps.info:http://dataapi.mljr.com/checkgps}")
    private String gpsInfoUrl;
    /**
     * 获取大数据API Token
     */
    @Value("${gps.token:http://dataapi.mljr.com/auth/queryToken?uuid=83c01f57ec8f416aa8be0ecacf0a4c99}")
    private String gpsTokenUrl;
    /**
     * 基于SpringBoot应用，获取配置应用名称
     */
    @Value("${spring.application.name:car-commons}")
    private String applicationName;
    @Autowired
    private DingRobotService dingRobotService;
    @Autowired
    private RedisService redisService;
    /**
     * 根据GPS设备号，获取第三方GPS设备号状态
     * <pre>
     *     1、接口中增加mock处理，为了测试使用；1：作为正常设备；2：作为非正常设备。以"S"开头的作为正常设备。
     *     2、该接口，一次查询的设备号，必须同一个供应商，默认按照赛格查询。
     *     3、接口中status为空正常；不为空时，一旦有以一个非正常，则未失败。
     * </pre>
     * @param ids GPS设备号
     * @param agencyType GPS设备商类型
     * @return
     */
    public Result<List<GpsDeviceInfoRe>> getGpsInfos(List<String> ids,GpsDeviceAgencyType agencyType){
        if(CollectionUtils.isEmpty(ids)){
            LOGGER.warn("{}请求参数不能为空",LOG_TITLE);
            return Result.failWithEmptyParam(Collections.emptyList());
        }
        List<GpsDeviceInfoRe> list = new ArrayList<>(DEFAULT_INITIAL_CAPACITY);
        long startTime = System.currentTimeMillis();
        try {
            String result = getGpsInfo(ids,agencyType);
            if (result == null) {
                dingTalk(ids,agencyType,"大数据GPS设备商认证失败，请求结果为空或者请求响应失败,设备ID="+JSON.toJSONString(ids));
                LOGGER.warn("{}失败,params:{}",LOG_TITLE, JSON.toJSONString(ids));
                return Result.fail(RemoteEnum.ERROR_IN_BUSINESS,"调用大数据GPS接口失败，请重试");
            }
            JSONObject object = JSON.parseObject(result);
            Integer code = object.getInteger("code");
            if (code == null || HttpStatus.SC_OK != code.intValue()) {
                dingTalk(ids,agencyType,"大数据GPS设备商认证失败，请求结果为空或者请求响应失败,code="+code+",设备ID="+ids.toString());
                LOGGER.warn("{}失败,params:{}",LOG_TITLE, JSON.toJSONString(ids));
                return Result.fail(RemoteEnum.ERROR_IN_BUSINESS,"调用大数据GPS接口失败，请重试");
            }
            JSONObject contextJson = object.getJSONObject("context");
            //当前请求时间
            Long currentTime = contextJson.getLong("current_time");
            JSONArray jsonArray = contextJson.getJSONArray("gps_infos");
            List<GpsDeviceInfo> deviceInfoList = jsonArray.toJavaList(GpsDeviceInfo.class);
            if(CollectionsTools.isEmpty(deviceInfoList)){
                dingTalk(ids,agencyType,"大数据GPS设备商认证失败，未查询相应GPS设备商状态信息"+JSON.toJSONString(ids));
                return Result.fail(RemoteEnum.ERROR_IN_BUSINESS,"未查询相应GPS设备商状态信息");
            }
            deviceInfoList.forEach(deviceInfo -> {
                String[] originStatusArray = deviceInfo.getStatus();
                // 数据为空是正常
                boolean normal = (null == originStatusArray || originStatusArray.length == 0);
                String deviceId = deviceInfo.getGpsId();
                GpsDeviceInfoRe deviceInfoRe = new GpsDeviceInfoRe();
                deviceInfoRe.setCity(deviceInfo.getCity());
                deviceInfoRe.setGpsId(deviceId);
                //对mock数据重置对象属性处理
                if(NORMAL_GPS.equals(deviceId) || deviceId.contains(NORMAL_PREFIX)){
                    deviceInfo.setStatus(NORMAL_STATUS);
                }else if(ABNORMAL_GPS.equals(deviceId) || deviceId.contains(ABNORMAL_PREFIX)){
                    deviceInfo.setStatus(ABNORMAL_STATUS);
                }
                originStatusArray = normal ? NORMAL_STATUS : deviceInfo.getStatus();
                List<String> gpsStatusList = Arrays.asList(originStatusArray);
                deviceInfoRe.setApplyTime(currentTime);
                List<String> statusDescList = new ArrayList<>(gpsStatusList.size());
                gpsStatusList.forEach(eachStatus -> statusDescList.add(CarGpsConstant.GPSDeviceStatusEnum.getName(eachStatus)));
                deviceInfoRe.setStatusCode(gpsStatusList.toArray(new String[0]));
                deviceInfoRe.setStatusDesc(statusDescList.toArray(new String[0]));
                if(CollectionsTools.isNotEmpty(gpsStatusList)){
                    //一旦有一个不正常的则为失败
                    normal = gpsStatusList.stream().filter(each -> !CarGpsConstant.GPSDeviceStatusEnum.NORMAL.getValue().equals(each)).count() == 0;
                }
                deviceInfoRe.setResult(normal ? STATUS_YES : STATUS_NO);
                list.add(deviceInfoRe);
            });
        } catch (Exception e) {
            dingTalk(ids,agencyType,"大数据GPS设备商认证查询异常"+e.getMessage());
            LOGGER.error("{}查询异常,gpsInfoUrl={},ids={},agencyType={}",LOG_TITLE,gpsInfoUrl, JSON.toJSON(ids),agencyType,e);
        }finally {
            recordDuration(System.currentTimeMillis() - startTime,ids,agencyType);
        }
        return Result.suc(list);
    }

    /**
     * 根据GPS设备号，获取第三方GPS设备号状态
     * @param ids GPS设备号
     * @return
     */
    @Deprecated
    public Result<List<GpsDeviceInfoRe>> getGpsInfos(List<String> ids){
        List<GpsDeviceInfoRe> reList = new ArrayList<>();
        try {
            if(CollectionUtils.isEmpty(ids)){
                LOGGER.warn("{}请求参数不能为空",LOG_TITLE);
                return Result.failWithEmptyParam(reList);
            }
            String result = getGpsInfo(ids,GpsDeviceAgencyType.segGPS);
            if (StringTools.isEmpty(result)) {
                LOGGER.warn("{}获取gps设备信息失败,params:{}",LOG_TITLE, JSON.toJSONString(ids));
                return Result.fail(RemoteEnum.ERROR_IN_BUSINESS,"获取gps设备信息失败，请重试");
            }
            JSONObject object = JSON.parseObject(result);
            if (object == null) {
                LOGGER.warn("{}获取gps设备信息失败,params:{}",LOG_TITLE, JSON.toJSONString(ids));
                return Result.fail(RemoteEnum.ERROR_IN_BUSINESS,"获取gps设备信息失败，请重试");
            }
            Integer code = object.getInteger("code");
            if (code == null || !code.equals(200)) {
                LOGGER.warn("{}调用GPS接口出错,params:{}",LOG_TITLE, JSON.toJSONString(ids));
                return Result.fail(RemoteEnum.ERROR_IN_BUSINESS,"获取gps设备信息失败，请重试");
            }
            JSONObject contextJson = object.getJSONObject("context");

            //当前请求时间
            Long currentTime = contextJson.getLong("current_time");
            JSONArray jsonArray = contextJson.getJSONArray("gps_infos");
            List<GpsDeviceInfo> gpsDeviceInfos = jsonArray.toJavaList(GpsDeviceInfo.class);

            if (gpsDeviceInfos == null || gpsDeviceInfos.size() == 0) {
                LOGGER.warn("{}调用GPS接口信息为空,params:{}",LOG_TITLE, JSON.toJSONString(ids));
                return Result.fail(RemoteEnum.ERROR_IN_BUSINESS,"获取gps设备信息失败，请重试");
            }else{
                LOGGER.info("{}查询结果,gpsDeviceInfos={}",LOG_TITLE, JSON.toJSONString(gpsDeviceInfos));
            }

            gpsDeviceInfos.forEach(gpsDeviceInfo -> {
                if(NORMAL_GPS.equals(gpsDeviceInfo.getGpsId())){
                    gpsDeviceInfo.setStatus(NORMAL_STATUS);
                }else if(ABNORMAL_GPS.equals(gpsDeviceInfo.getGpsId())){
                    gpsDeviceInfo.setStatus(UN_NORMAL_STATUS);
                }
                String[] status = gpsDeviceInfo.getStatus();
                if(status == null || status.length == 0){
                    gpsDeviceInfo.setStatus(new String[] { "NORMAL" });
                    status = gpsDeviceInfo.getStatus();
                }
                List<String> listStatus =  Arrays.asList(status);
                List<String> statusName = new ArrayList<>();
                listStatus.forEach(per -> statusName.add(CarGpsConstant.GPSDeviceStatusEnum.getName(per)));

                GpsDeviceInfoRe gpsDeviceInfoRe = new GpsDeviceInfoRe();
                if ("NORMAL".equals(status[0])) {
                    gpsDeviceInfoRe.setResult(STATUS_YES);
                } else {
                    gpsDeviceInfoRe.setResult(STATUS_NO);
                }
                gpsDeviceInfoRe.setGpsId(gpsDeviceInfo.getGpsId());
                gpsDeviceInfoRe.setCity(gpsDeviceInfo.getCity());
                gpsDeviceInfoRe.setStatusCode(status);
                gpsDeviceInfoRe.setStatusDesc(statusName.toArray(new String[0]));
                gpsDeviceInfoRe.setApplyTime(currentTime);
                reList.add(gpsDeviceInfoRe);
            });
        } catch (Exception e) {
            LOGGER.error("{}查询异常,gpsInfoUrl={},ids={}",LOG_TITLE,gpsInfoUrl, JSON.toJSON(ids),e);
        }
        return  Result.suc(reList);
    }

    /**
     * 调用第三方大数据结果
     * @param ids GPS设备号
     * @param agencyType GPS设备商类型
     * @return
     */
    private String getGpsInfo(List<String> ids,GpsDeviceAgencyType agencyType){
        String result = null;
        try {
            LOGGER.info("gps调用大数据接口-获取token-url:{}", gpsTokenUrl);
            String json = OkHttpUtil.get(gpsTokenUrl);
            assertObj(json,"取GpsToken信息失败json为空");
            GpsDeviceRespDTO<GpsDeviceTokenDTO> gpsDeviceRespDTO = JSON.parseObject(json,new TypeReference<GpsDeviceRespDTO<GpsDeviceTokenDTO>>(){});
            assertObj(gpsDeviceRespDTO,"取GpsToken信息失败，token为空");
            assertObj(gpsDeviceRespDTO.getContext().getToken(),"取GpsToken信息失败，token为空");
            String token = gpsDeviceRespDTO.getContext().getToken();
            String url = getFullUrl(ids,agencyType);
            Map<String,String> map = new HashMap<>(2);
            map.put("user-sign",token);
            LOGGER.info("gps调用大数据接口-获取Full-url:{}", url);
            //进行http请求
            result = OkHttpUtil.get(url,map,response -> {
                if (!response.isSuccessful()) {
                    LOGGER.warn("GPS自动验证失败：apiUrl={},responseStatusCode={},responseMessage={},response={}",url,response.code(),response.message(),response.toString());
                    dingTalk(ids, agencyType, MessageFormat.format("大数据GPS设备商认证失败,responseStatusCode={0}", response.code()));
                }
            });
            LOGGER.info("{}查询result={},ids={},agencyType={}",LOG_TITLE,result,ids.toString(),agencyType.name());
        } catch (Exception e) {
            dingTalk(ids,agencyType,"大数据GPS设备商认证查询异常:"+e.getMessage());
            LOGGER.error("{}查询异常,ids={}",LOG_TITLE, JSON.toJSON(ids),e);
        }
        return result;
    }

    private void assertObj(Object obj,String message){
        Optional.ofNullable(obj).orElseThrow(() -> new GpsDeviceException(message));
    }

    /**
     * 获取完整的GPS设备号状态验证URL
     * @param ids
     * @param agencyType
     * @return
     */
    private String getFullUrl(List<String> ids,GpsDeviceAgencyType agencyType){
        String url = gpsInfoUrl + "/" + StringTools.valueOfList(ids) + "?uniqueId="+System.currentTimeMillis();
        if(null != agencyType){
            url += "&logo=" + agencyType.name();
        }
        return url;
    }

    /**
     * GPS验证耗时计入Redis
     * 按天统计 HK:时分 HV:每个设备号耗时
     * @param duration 毫秒
     * @param gpsNos 设备集合
     * @param agencyType GPS设备类型
     */
    private void recordDuration(long duration,List<String> gpsNos,GpsDeviceAgencyType agencyType){
        try {
            Date currentDay = TimeTools.createNowTime();
            String daily = TimeTools.format(currentDay,"yyyyMMdd");
            String redisKey = MessageFormat.format("gpsDuration:{0}:{1}",agencyType.name(),daily);
            String redisHK = TimeTools.format(currentDay,"HHmm");
            Object absentHV = redisService.hashOperations().get(redisKey,redisHK);
            CompletableFuture.runAsync(() -> {
                for(String gpsNo : gpsNos){
                    Map<String,Long> hashValue;
                    if(null != absentHV){
                        hashValue = JSONObject.parseObject(absentHV.toString(),new TypeReference<Map<String,Long>>(){});
                    }else{
                        hashValue = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
                    }
                    if(null == hashValue.get(gpsNo)){
                        hashValue.put(gpsNo,duration);
                    }else{
                        //设备可以重复调用接口，以最大一次耗时作为最终存储
                        if(Long.valueOf(duration).compareTo(hashValue.get(gpsNo)) == 1){
                            hashValue.put(gpsNo,duration);
                        }
                    }
                    redisService.hashOperations().put(redisKey,redisHK,JSONObject.toJSONString(hashValue));
                }
                /**
                 * 不设置，统计查询需要
                 * {@link org.springframework.data.redis.core.HashOperations#multiGet(Object, Collection)}
                 */
                long firstExpire = redisService.hashOperations().getOperations().getExpire(redisKey, TimeUnit.DAYS);
                if(firstExpire <= 0){
                    redisService.hashOperations().getOperations().expire(redisKey,15, TimeUnit.DAYS);
                }
            });
        } catch (Exception e) {
            LOGGER.warn("GPS设备耗时设置redis异常,gpsNos={}",gpsNos.toArray(),e);
        }
    }
    /**
     * 钉钉报警
     * @param agencyType GPS设备商类型
     * @param gpsIds GPS设备号
     * @param log 内容
     */
    public void dingTalk(List<String> gpsIds,GpsDeviceAgencyType agencyType,String log){
        try {
            //这里忽略安装信息变更其中一个设备号
            String redisKey = REDIS_DING_PREFIX + String.join(",",gpsIds);
            redisService.incr(redisKey.getBytes(),REDIS_DING_EXPIRE);
            String nowDate = TimeTools.format(TimeTools.createNowTime(),"yyyyMMdd");
            String hashKey = REDIS_DING_PREFIX + agencyType.name();
            for(String each : gpsIds){
                String increaseCountKey = REDIS_DING_PREFIX + "incr:" + nowDate + ":" + each ;
                redisService.incr(increaseCountKey.getBytes(),REDIS_DING_EXPIRE);
                long eachValue = redisService.getIncrValue(increaseCountKey);
                Map<String,Long> hashValue;
                Object presentValue  = redisService.hashOperations().get(hashKey,nowDate);
                if(null != presentValue){
                    hashValue = JSONObject.parseObject(presentValue.toString(),new TypeReference<Map<String,Long>>(){});
                }else{
                    hashValue = Maps.newHashMap();
                }
                hashValue.put(each,eachValue);
                redisService.hashOperations().put(hashKey,nowDate,JSON.toJSONString(hashValue));
            }
            long currentValue = redisService.getIncrValue(redisKey);
            if(currentValue <= REDIS_DING_THRESHOLD){
                return;
            }
            if(gpsIds.contains(NORMAL_GPS) || gpsIds.contains(ABNORMAL_GPS)){
                LOGGER.info("{}mock gps device",LOG_TITLE ,gpsIds.toString());
                return;
            }
            String fullContent = MessageFormat.format("{0}【认证失败次数{1}超过阈值{2}，所以要发送报警咯^_^】",log,currentValue,REDIS_DING_THRESHOLD);
            CompletableFuture.runAsync(() -> {
                MarkdownDingRobotReq robotReq = new MarkdownDingRobotReq();
                robotReq.setTitle(MONITOR_LOG_TITLE);
                StringBuffer content = new StringBuffer("### ").append(MONITOR_LOG_TITLE);
                content.append("\n\n ### 所属应用：").append(applicationName);
                content.append("\n\n ### 请求时间：").append(TimeTools.format4YYYYMMDDHHMISS(TimeTools.createNowTime()));
                content.append("\n\n ### gpsTokenUrl：").append(gpsTokenUrl);
                content.append("\n\n ### gpsInfoUrl：").append(getFullUrl(gpsIds,agencyType));
                content.append("\n\n ### 报警内容：\n\n > ").append(fullContent);
                robotReq.setText(content.toString());
                dingRobotService.sendMarkdown(DingAuth.NORMAL_TOKEN, robotReq);
            });
        } catch (Exception e) {
            //ignore
        }
    }
}