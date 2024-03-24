package com.example.gateway.redis;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private static final String key = "token:";
    @Resource
    private final ReactiveRedisOperations<String, Auth> ops;

    public AuthService(ReactiveRedisOperations<String, Auth> ops) {
        this.ops = ops;
    }

    public Mono<Auth> checkToken(@NotBlank String hashKey) {
        return ops.opsForValue().get(key + hashKey);
    }

    public Mono<Auth> get(@NotBlank String hashKey) {
        return ops.opsForValue().get(key + hashKey);
    }

    public Mono<Long> post(Auth auth) {
        return ops.opsForValue().increment(key) //
                .doOnNext(id -> {
                    auth.setId(String.valueOf(id));
                    ops.opsForValue().set(key + id, auth).subscribe();
                });
    }

    public Mono<Boolean> delete(Auth auth) {
        return ops.opsForValue().delete(key + auth.getId());
    }

}