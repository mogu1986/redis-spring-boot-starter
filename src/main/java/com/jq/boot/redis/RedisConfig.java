package com.jq.boot.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jim
 * @date 2019/4/17 017 13:55
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params)-> {
            StringBuilder sb = new StringBuilder();

            String[] value = new String[1];
            Cacheable cacheable = method.getAnnotation(Cacheable.class);
            if (cacheable != null) {
                value = cacheable.value();
            }
            CachePut cachePut = method.getAnnotation(CachePut.class);
            if (cachePut != null) {
                value = cachePut.value();
            }
            CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
            if (cacheEvict != null) {
                value = cacheEvict.value();
            }
            sb.append(method.getName());
            sb.append(":").append(value[0]);

            //获取参数值
            for (Object obj : params) {
                sb.append(":" + String.valueOf(obj));
            }
            logger.info("缓存 key = {}", sb.toString());
            return sb;
        };
    }

}
