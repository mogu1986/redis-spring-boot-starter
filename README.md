# redis-spring-boot-starter

## 介绍
redis-spring-boot-starter基于springboot + redis,可以很方便的集成redis到springboot中,自带RedisUtil工具类,提供常用方法及自动过期时间(可配置,默认1小时)

## 使用说明

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.jq.boot</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置参数,其中`spring.redis.expire`为新增加的配置,表示过期时间,其他与spring-boot-starter-redis配置一致

```yaml
spring:
  redis:
    host: ${redis.host}
    port: ${redis.port}
    password: ${redis.password}
    database: 0
    expire: 3600
    jedis    
      pool:
        xxx
    lettuce:
      pool:
        xxx
```

### 3. 代码中使用

```java
@Autowired
private RedisUtil redisUtil;

@Cacheable(value = "RedisTest")
public String get(String key){
    System.out.println("进入get方法");
    return redisUtil.get(key);
}

@CacheEvict(value = "RedisTest", allEntries = true)
public void set(String key, String value){
    redisUtil.set(key, value);
}
```