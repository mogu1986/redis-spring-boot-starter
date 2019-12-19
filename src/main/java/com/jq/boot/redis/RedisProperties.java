package com.jq.boot.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jim
 * @date 2019/1/24 024 13:35
 */
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    /**
     * 自动过期时间,单位秒(默认1小时)
     */
    private long expire = 60 * 60;

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
