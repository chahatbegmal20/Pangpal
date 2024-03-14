package dev.n7meless.config;

import dev.n7meless.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.NoSuchElementException;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String , User> redisTemplate() {
        RedisTemplate<String, User> template = new RedisTemplate<>();

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(User.class));
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
//                .initialCacheNames(Set.of("user", "users"))
                .withCacheConfiguration("user",
                        RedisCacheConfiguration.defaultCacheConfig()
//                                .disableCachingNullValues()
                                .entryTtl(Duration.ofSeconds(60)))
                .withCacheConfiguration("users",
                        RedisCacheConfiguration.defaultCacheConfig()
//                                .disableCachingNullValues()
                                .entryTtl(Duration.ofSeconds(10)));
    }
}
