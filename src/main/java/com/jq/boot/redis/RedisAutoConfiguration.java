package com.jq.boot.redis;

import com.jq.boot.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author jim
 * @date 2019/1/24 024 10:12
 */
@Configuration
@ConditionalOnClass({RedisUtil.class})
@AutoConfigureBefore({org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class})
@EnableConfigurationProperties({RedisProperties.class, org.springframework.boot.autoconfigure.data.redis.RedisProperties.class})
@EnableCaching
public class RedisAutoConfiguration {
    @Value("${spring.jackson.date-format?:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;

    @Autowired
    private com.jq.boot.redis.RedisProperties properties;

    @Autowired
    private RedisConnectionFactory factory;
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<Object, Object> redisTemplate(){
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(keySerializer());
        template.setHashKeySerializer(keySerializer());
        template.setValueSerializer(valueSerializer());
        template.setHashValueSerializer(valueSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(properties.getExpire()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .transactionAware()
                .build();

        return redisCacheManager;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
    @Bean
    @ConditionalOnMissingBean
    public RedisUtil redisUtil(){
        return new RedisUtil();
    }
}
