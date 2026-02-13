package com.axiom.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import com.google.gson.GsonBuilder;
import com.redis.om.spring.client.RedisModulesClient;
import com.redis.om.spring.ops.RedisModulesOperations;

@Configuration
@EnableRedisRepositories(basePackages = "com.axiom.booking.repository")
public class RedisConfig {

    /**
     * Connects to Redis using default host/port.
     * You can override via application.yml
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // defaults to localhost:6379
    }

    /**
     * Redis OM operations bean
     * Supports storing entities, indexing, and vector search.
     */
    @Bean
    public RedisModulesOperations<String> redisModulesOperations(
            RedisModulesClient redisModulesClient, 
            StringRedisTemplate stringRedisTemplate, 
            GsonBuilder gsonBuilder) {
        // Explicitly add <String> to the constructor call
        return new RedisModulesOperations<String>(redisModulesClient, stringRedisTemplate, gsonBuilder);
    }
}
