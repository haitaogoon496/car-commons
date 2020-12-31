//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mljr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @description: 读取properties文件
 * @Date : 2018/1/6 上午11:58
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public final class PropertiesReader {
    private static final Logger CLASS_LOGGER = LoggerFactory.getLogger(PropertiesReader.class);
    private static Map<String, Properties> propertiesMap = new ConcurrentHashMap();
    private static final String DEFAULT = "store";
    private static final List CLASSES_TYPES = Arrays.asList(Integer.class, Boolean.class, String.class, Long.class, Double.class);

    private PropertiesReader() {
    }

    private static <T> void isThisClass(Class<T> clazz){
        if(!CLASSES_TYPES.contains(clazz)){
            throw new RuntimeException("参数“clazz” 类型不在指定的类型中！");
        }
    }

    public static <T> T getAppointPropertiesAttribute(String propertiesName, String attributeName, Class<T> clazz) {
        if(null == clazz){
            throw new RuntimeException("获取指定类型“clazz”参数为null !");
        }
        isThisClass(clazz);
        Properties ps = getProperties(propertiesName);
        return getAppointPropertiesAttribute(ps, attributeName, clazz);
    }

    public static <T> T getAppointPropertiesAttribute(Properties ps, String attributeName, Class<T> clazz) {
        if(null == clazz){
            throw new RuntimeException("获取指定类型“clazz”参数为null !");
        }
        isThisClass(clazz);
        if (ps != null) {
            String value = ps.getProperty(attributeName);
            if (value != null && !"".equals(value)) {
                if (clazz == Boolean.class) {
                    return clazz.cast(Boolean.parseBoolean(value));
                } else if (clazz == Integer.class) {
                    return clazz.cast(Integer.parseInt(value));
                } else if (clazz == Long.class) {
                    return clazz.cast(Long.parseLong(value));
                } else {
                    return clazz == Double.class ? clazz.cast(Double.parseDouble(value)) : clazz.cast(value);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Properties getProperties(String propertiesName) {
        return getProperties(propertiesName, true);
    }

    public static Properties getProperties(String propertiesName, boolean isCache) {
        Properties resultProperties = propertiesMap.get(propertiesName);
        if (resultProperties == null) {
            Properties defaultProp = createProperties(propertiesName + DEFAULT);
            resultProperties = createProperties(propertiesName);
            if (defaultProp != null && resultProperties != null) {
                resultProperties.putAll(defaultProp);
            } else if (defaultProp != null) {
                resultProperties = defaultProp;
            }

            if (isCache && resultProperties != null) {
                propertiesMap.put(propertiesName, resultProperties);
            }
        }

        return resultProperties;
    }
    private static Properties createProperties(String propertiesName) {
        InputStream fis;
        Properties properties = null;

        try {
            fis = PropertiesReader.class.getResourceAsStream("/" + propertiesName + ".properties");
            if (fis != null) {
                properties = new Properties();
                properties.load(fis);
                fis.close();
            }
        } catch (Exception var4) {
            CLASS_LOGGER.error(var4.getMessage(), var4);
        }
        return properties;
    }
}
