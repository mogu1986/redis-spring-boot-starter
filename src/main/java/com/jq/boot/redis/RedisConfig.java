package com.jq.boot.redis;

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
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params)-> {
            StringBuilder sb = new StringBuilder();
            String[] value = new String[1];
            // sb.append(target.getClass().getName());
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
            sb.append(value[0]);
            sb.append(":").append(method.getName());
            //获取参数值
            for (Object obj : params) {
                sb.append(":" + String.valueOf(obj));
            }
            System.out.println("缓存key="+sb.toString());
            return sb;
        };
    }
}
