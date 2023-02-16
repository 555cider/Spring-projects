package com.piano.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.piano.gateway.auth.Auth;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

//    @Value("#{environment['spring.data.redis.host'] == null ? 'localhost' : environment['spring.data.redis.host']}")
//    private String host;
//
//    @Value("#{environment['spring.data.redis.port'] == null ? '6739' : environment['spring.data.redis.port']}")
//    private int port;

    @Bean
    ReactiveRedisOperations<String, Auth> reactiveRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory) {
	StringRedisSerializer stringSerializer = new StringRedisSerializer();
	Jackson2JsonRedisSerializer<Auth> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Auth.class);
	RedisSerializationContext<String, Auth> context = //
		RedisSerializationContext.<String, Auth>newSerializationContext() //
			.key(stringSerializer) //
			.value(jacksonSerializer) //
			.hashKey(stringSerializer) //
			.hashValue(jacksonSerializer) //
			.build();
	return new ReactiveRedisTemplate<>(redisConnectionFactory, context);
    }

}