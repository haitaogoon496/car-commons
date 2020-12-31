package com.mljr.redis.service;

import com.mljr.redis.enums.KeyPrefix;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * @description: 统一Redis Service接口
 * @Date : 2018/4/23 下午3:30
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface RedisService {
    /**
     * 获取统一的业务应用前缀，用于生成RedisKey
     * @return
     */
    KeyPrefix getKeyPrefix();
    /**
     * 根据key，获取如下真正的要存储的key，以避免覆盖
     * @param key
     * @return
     */
    String getKeyWithSystemCode(String key);
    /**
     * 根据key，获取如下真正的要存储的key，以避免覆盖
     * @param key
     * @param systemType 系统来源
     * @return
     */
    @Deprecated
    String getKeyWithSystemCode(String key, String systemType);
    /**
     * 加锁
     * @param lockKey
     * @param liveTime 失效时间
     */
    boolean lock(String lockKey,long liveTime);
    /**
     * 加锁
     * @param lockKey
     */
    boolean lock(String lockKey);
    /**
     * 解锁
     * @param lockKey
     */
    void unlock(String lockKey);
    /**
     * 通过key删除
     */
    Object del(String... keys);
    /**
     * 添加key value 并且设置存活时间(byte)
     * @param key
     * @param value
     * @param liveTime 单位秒
     */
    void set(byte[] key, byte[] value, long liveTime);
    /**
     * 添加key value 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime  单位秒
     */
    <V> void set(String key, V value, long liveTime);
    /**
     * 添加key value
     * @param key
     * @param value
     */
    <V> void set(String key, V value);
    /**
     * 添加key value，如果存在返回false,否则返回true 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime  单位秒
     * @return 如果存在返回false,否则返回true
     */
    <V> boolean setNx(String key, V value, long liveTime);
    /**
     * 添加key value，如果存在返回false,否则返回true
     * @param key
     * @param value
     * @return 如果存在返回false,否则返回true
     */
    <V> boolean setNx(String key, V value);

    /**
     * 添加key value（该key通过接口内部再次加工）
     * @param key
     * @param value
     */
    <V> void setWithPrefix(String key, V value);
    /**
     * 添加key value（该key通过接口内部再次加工）
     * @param key
     * @param value
     * @param liveTime  单位秒
     */
    <V> void setWithPrefix(String key, V value, long liveTime);
    /**
     * 添加key value (字节)(序列化)
     * @param key
     * @param value
     */
    void set(byte[] key, byte[] value);
    /**
     * 自增长
     * @param key
     * @param liveTime
     */
    void incr(final byte[] key,final long liveTime);
    /**
     * 返回增长值
     * @param key
     * @return
     */
    long getIncrValue(final String key);
    /**
     * 获取redis value
     * @param key
     * @return
     */
    <T> T get(String key, Class<T> clazz);
    /**
     * 获取redis value（该key通过接口内部再次加工）
     * @param key
     * @return
     */
    <T> T getWithPrefix(String key, Class<T> clazz);
    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     */
    Set<String> setkeys(String pattern);
    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    boolean exists(String key);
    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    boolean existsWithPrefix(String key);
    /**
     * 清空redis 所有数据
     * @return
     */
    String flushDB();
    /**
     * 查看redis里有多少数据
     */
    long dbSize();
    /**
     * 检查是否连接成功
     *
     * @return
     */
    String ping();

    HashOperations<String, String, Object> hashOperations();

    ValueOperations<String, Object> valueOperations();

    ListOperations<String, Object> listOperations();

    SetOperations<String, Object> setOperations();

    ZSetOperations<String, Object> zSetOperations();
}