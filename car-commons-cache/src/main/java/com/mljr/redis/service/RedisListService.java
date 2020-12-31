package com.mljr.redis.service;

import java.util.List;

/**
 * @description: List数据结果操作
 * @Date : 2018/12/14 下午2:38
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public interface RedisListService {
    /**
     * Append {@code values} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param values
     * @return
     * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
     */
    <T> Long rPush(String key, T... values);

    /**
     * Prepend {@code values} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param values
     * @return
     * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
     */
    <T> Long lPush(String key, T... values);

    /**
     * Append {@code values} to {@code key} only if the list exists.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
     */
    <T> Long rPushX(String key, T value);

    /**
     * Prepend {@code values} to {@code key} only if the list exists.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="http://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
     */
    <T> Long lPushX(String key, T value);

    /**
     * Get the size of list stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="http://redis.io/commands/llen">Redis Documentation: LLEN</a>
     */
    Long lLen(String key);

    /**
     * Get elements between {@code start} and {@code end} from list at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
     */
    <T> List<T> lRange(String key, long start, long end);
}
