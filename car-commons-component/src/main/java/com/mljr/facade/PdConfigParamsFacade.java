package com.mljr.facade;

import com.mljr.model.PdConfigParams;
import com.mljr.redis.service.RedisService;
import com.mljr.service.PdConfigParamsService;
import com.mljr.util.CollectionsTools;
import com.mljr.util.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 产品全局参数Facade
 * <pre>
 *     如果相关应用需要使用该组件，需要添加PdConfigParamsMapper.xml到 com.mljr.mapper包中，且MybatisConfig的baseMapper需要加载com.mljr.mapper。
 * </pre>
 * @Date : 下午5:07 2018/4/11
 * @Author : 石冬冬-Heil Hitler(dongdong.shi@mljr.com)
 */
@Component
public class PdConfigParamsFacade {
    private final String LOG_TITLE = "【产品全局参数】";
    /**
     * 开关开启
     */
    private final List<String> ENABLE_SWITCH_VALUES = Arrays.asList("1","yes","true","on");
    /**
     * 开关关闭
     */
    private final List<String> DISABLED_SWITCH_VALUES = Arrays.asList("0","no","false","off");
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PdConfigParamsService pdConfigParamsService;
    @Autowired
    private RedisService redisService;
    /**
     * 默认全局参数
     */
    private final long ALIVE_TIME = 0;

    /**
     * 获取系统全局配置表中的所有数据，统一刷新到Redis缓存
     */
    public void flushRedis(){
       try {
            List<PdConfigParams> list = this.pdConfigParamsService.queryListForFlushRedis();
            if(CollectionsTools.isNotEmpty(list)){
                for(PdConfigParams param : list){
                    flushRedis(param.getParamKey());
                }
            }
        } catch (Exception e) {
            LOGGER.error("{}刷新redis异常",LOG_TITLE,e);
        }
    }

    /**
     * 按照参数key刷新到redis中
     * @param paramKey
     */
    public PdConfigParams flushRedis(String paramKey){
        PdConfigParams fetch = null;
        try {
            PdConfigParams pdConfigParams = pdConfigParamsService.queryByParamKey(paramKey);
            if(null != pdConfigParams){
                String paramValue = pdConfigParams.getParamValue();
                redisService.setWithPrefix(paramKey,paramValue,ALIVE_TIME);
                String redisValue = redisService.getWithPrefix(paramKey,String.class);
                LOGGER.info("{}load redis,key={},paramValue={},redisValue={}",LOG_TITLE,paramKey, paramValue,redisValue);
            }
        } catch (Exception e) {
            LOGGER.error("{}刷新redis异常",LOG_TITLE,e);
        }
        return fetch;
    }

    /**
     * 根据 paramKey 查询 参数对象
     * 优先从Redis缓存中取，没有的话，设置到Redis中
     * @param paramKey
     * @return
     */
    public String queryValueByParamKey(@NotNull String paramKey) {
        String redisValue = null;
        PdConfigParams pdConfigParams;
        try {
            if(redisService.existsWithPrefix(paramKey)){
                redisValue = redisService.getWithPrefix(paramKey,String.class);
            }else{
                pdConfigParams = pdConfigParamsService.queryByParamKey(paramKey);
                if(null != pdConfigParams){
                    redisValue = pdConfigParams.getParamValue();
                    redisService.setWithPrefix(paramKey,redisValue,ALIVE_TIME);
                }
            }
        } catch (Exception e) {
            pdConfigParams = pdConfigParamsService.queryByParamKey(paramKey);
            redisValue = pdConfigParams.getParamValue();
        }
        return redisValue;
    }

    /**
     * 根据 paramKey 查询 开关
     * @param paramKey
     * @return
     */
    public boolean getSwitchValue(@NotNull String paramKey) {
        boolean switchValue = false;
        try {
            PdConfigParams pdConfigParams = pdConfigParamsService.queryByParamKey(paramKey);
            if(null != pdConfigParams){
                String paramValue = pdConfigParams.getParamValue();
                switchValue = ENABLE_SWITCH_VALUES.contains(paramValue.toLowerCase());
            }
        } catch (Exception e) {
            LOGGER.error("{}getSwitchValue异常，paramKey={}",LOG_TITLE,paramKey,e);
        }
        return switchValue;
    }

    /**
     * 根据 paramKey 查询 参数对象
     * @param paramKey
     * @param clazz 泛型类
     * @param <T> 具体泛型类
     * @return
     */
    public <T> T getValueByParamKey(@NotNull String paramKey, Class<T> clazz) {
        T value = null;
        String classSimpleName = clazz.getSimpleName();
        String paramValue = queryValueByParamKey(paramKey);
        if(null != paramValue){
            switch (classSimpleName){
                case "String":
                    value = (T)paramValue;
                    break;
                case "Integer":
                    value = (T)Integer.valueOf(paramValue);
                    break;
                case "Long":
                    value = (T)Long.valueOf(paramValue);
                    break;
                case "Double":
                    value = (T)Double.valueOf(paramValue);
                    break;
                case "Short":
                    value = (T)Short.valueOf(paramValue);
                    break;
                case "BigDecimal":
                    value = (T) new BigDecimal(paramValue);
                    break;
                case "Boolean":
                    if(null != paramValue && ENABLE_SWITCH_VALUES.contains(paramValue.toLowerCase())){
                        value = (T)Boolean.TRUE;
                    }
                    if(null != paramValue && DISABLED_SWITCH_VALUES.contains(paramValue.toLowerCase())){
                        value = (T)Boolean.FALSE;
                    }
                    break;
                default:
                    break;
            }
        }
        return value;
    }

    /**
     * 根据 paramKey 查询 参数对象
     * @param paramKey
     * @return
     */
    public <T> List<T> convertValueToListByParamKey(@NotNull String paramKey) {
        List<T> list = new ArrayList<>();
        String paramValue = queryValueByParamKey(paramKey);
        if(null != paramValue){
            if(StringTools.isNotEmpty(paramValue)){
                String[] arrays = paramValue.split(",");
                for(String value : arrays){
                    list.add((T)value);
                }
            }
        }
        return list;
    }

    public int insert(PdConfigParams pdConfigParams) {
        return pdConfigParamsService.savePdConfigParams(pdConfigParams);
    }

    public static void main(String[] args) {
        PdConfigParamsFacade facade = new PdConfigParamsFacade();
        System.out.println(facade.getValueByParamKey("name",String.class));
        System.out.println(facade.getValueByParamKey("name",BigDecimal.class));
        System.out.println(facade.getValueByParamKey("name",Double.class));
        System.out.println(facade.getValueByParamKey("name",Integer.class));
        System.out.println(facade.getValueByParamKey("name",Long.class));
        System.out.println(facade.getValueByParamKey("name",Boolean.class));
        System.out.println(facade.convertValueToListByParamKey("name"));
    }
}
