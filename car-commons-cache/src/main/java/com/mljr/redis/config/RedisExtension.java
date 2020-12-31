package com.mljr.redis.config;

import com.mljr.redis.enums.KeyPrefix;
import lombok.Data;

/**
 * @description: Redis相关依赖配置扩展
 * @Date : 2018/12/13 下午12:14
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
@Data
public class RedisExtension {
    /**
     * 获取统一的业务应用前缀，用于生成RedisKey
     * @return
     */
    KeyPrefix keyPrefix;
}
