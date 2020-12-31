package com.mljr.redis.service;

import com.mljr.redis.config.RedisExtension;
import com.mljr.redis.enums.KeyPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

/**
 * @description: 统一Redis Service接口
 * @Date : 2018/4/23 下午3:30
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Component("redisUtil")
public class RedisUtil implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    /**
     * 默认重试次数5次
     */
    private final int  RETRY_TIME = 5;
    /**
     * 默认失效时间为10秒
     */
    private final long EXPIRE_TIME = 10;

    @Autowired
    public RedisTemplate<String,Object> redisTemplate;
    @Value(value = "${system_code:}")
    private String systemCode;
    @Autowired
    private RedisExtension configExtend;
    @Autowired
    HashOperations<String, String, Object> hashOperations;
    @Autowired
    ValueOperations<String, Object> valueOperations;
    @Autowired
    ListOperations<String, Object> listOperations;
    @Autowired
    SetOperations<String, Object> setOperations;
    @Autowired
    ZSetOperations<String, Object> zSetOperations;

    @Override
    public KeyPrefix getKeyPrefix() {
        return configExtend.getKeyPrefix();
    }

    @Override
    public String getKeyWithSystemCode(String key) {
        String systemType = null;
        if(StringUtils.isEmpty(systemCode)) {
            // TODO
            //systemType = SystemSourceHandler.get().getName();
        } else {
            systemType = "100000".equals(systemCode) ? "zy" : "qd";
        }
        return new StringBuffer(getKeyPrefix().getName()).append(":").append(systemType).append(":").append(key).toString();
    }

    @Deprecated
    @Override
    public String getKeyWithSystemCode(String key, String systemType) {
        return new StringBuffer(getKeyPrefix().getName()).append(":").append(systemType).append(":").append(key).toString();
    }

    /**
     * 加锁
     * @param lockKey
     * @param liveTime 失效时间
     */
    @Override
    public boolean lock(String lockKey,long liveTime){
        boolean result = false;
        for(int i = 0;i < RETRY_TIME;i++) {
            try {
                result = this.setNx(lockKey,"1",liveTime);
                if(result){
                    break;
                }
                Thread.sleep(100);
            } catch (Exception e) {
                logger.error("获取redis锁出现异常。");
            }
        }
        return result;
    }

    /**
     * 加锁
     * @param lockKey
     */
    @Override
    public boolean lock(String lockKey){
        return lock(lockKey,EXPIRE_TIME);
    }

    /**
     * 解锁
     * @param lockKey
     */
    @Override
    public void unlock(String lockKey){
        this.del(lockKey);
    }

    /**
     * @param keys
     */
    @Override
    public Object del(final String... keys) {
        return redisTemplate.execute((RedisCallback) connection -> {
            long result = 0;
            for (int i = 0; i < keys.length; i++) {
                result = connection.del(keys[i].getBytes());
            }
            return result;
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    @Override
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.set(key, value);
            if (liveTime > 0) {
                connection.expire(key, liveTime);
            }
            return 1L;
        });
    }

    @Override
    public <V> boolean setNx(String key, V value, long liveTime) {
        Boolean res = (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            byte[] k = key.getBytes();
            Boolean result = connection.setNX(k, SerializationUtils.serialize(value));
            if (liveTime > 0) {
                connection.expire(k, liveTime);
            }
            return result;
        });
        return Optional.ofNullable(res).orElse(false);
    }

    @Override
    public <V> boolean setNx(String key, V value) {
        return setNx(key,value,300);
    }

    /**
     * @param key
     * @param liveTime
     */
    @Override
    public void incr(final byte[] key,final long liveTime) {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.incrBy(key,1);
            if (liveTime > 0) {
                connection.expire(key, liveTime);
            }
            return 1L;
        });
    }
    @Override
    public long getIncrValue(final String key) {
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer=redisTemplate.getStringSerializer();
            byte[] rowkey=serializer.serialize(key);
            byte[] rowval=connection.get(rowkey);
            try {
                String val=serializer.deserialize(rowval);
                return Long.parseLong(val);
            } catch (Exception e) {
                return 0L;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    @Override
    public <V> void set(String key, V value, long liveTime) {
        this.set(key.getBytes(), SerializationUtils.serialize(value), liveTime);
    }

    /**
     * @param key
     * @param value
     */
    @Override
    public <V> void set(String key, V value) {
        this.set(key, value, 0L);
    }

    @Override
    public <V> void setWithPrefix(String key, V value) {
        set(getKeyWithSystemCode(key),value);
    }

    @Override
    public <V> void setWithPrefix(String key, V value, long liveTime) {
        set(getKeyWithSystemCode(key),value,liveTime);
    }

    /**
     * @param key
     * @param value
     */
    @Override
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.execute((RedisCallback<Object>) connection -> connection.get(key.getBytes()));
        if(null != value){
            T t = (T)SerializationUtils.deserialize((byte[])value);
            return t;
        }
        return null;
    }

    @Override
    public <T> T getWithPrefix(String key, Class<T> clazz) {
        return get(getKeyWithSystemCode(key),clazz);
    }

    /**
     * @param pattern
     * @return
     */
    @Override
    public Set<String> setkeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public boolean exists(final String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes()));
    }

    @Override
    public boolean existsWithPrefix(String key) {
        return exists(getKeyWithSystemCode(key));
    }

    /**
     * @return
     */
    @Override
    public String flushDB() {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }

    /**
     * @return
     */
    @Override
    public long dbSize() {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.dbSize());
    }

    /**
     * @return
     */
    @Override
    public String ping() {
        return redisTemplate.execute((RedisCallback<String>) connection -> connection.ping());
    }

    @Override
    public HashOperations<String, String, Object> hashOperations() {
        return hashOperations;
    }

    @Override
    public ValueOperations<String, Object> valueOperations() {
        return valueOperations;
    }

    @Override
    public ListOperations<String, Object> listOperations() {
        return listOperations;
    }

    @Override
    public SetOperations<String, Object> setOperations() {
        return setOperations;
    }

    @Override
    public ZSetOperations<String, Object> zSetOperations() {
        return zSetOperations;
    }

    private RedisUtil() {

    }
}
