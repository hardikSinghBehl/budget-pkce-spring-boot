package com.behl.ehrmantraut.security.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.behl.ehrmantraut.dto.UserAuthenticationDto;
import com.behl.ehrmantraut.security.configuration.properties.PkceConfigurationProperties;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(PkceConfigurationProperties.class)
public class InMemoryCodeCache {

    private final PkceConfigurationProperties pkceConfigurationProperties;

    @Bean
    public LoadingCache<String, UserAuthenticationDto> loadingCache() {
        final var expirationMinutes = pkceConfigurationProperties.getSecurity().getCodeExpirationMinutes();
        return CacheBuilder.newBuilder().expireAfterWrite(expirationMinutes, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    public UserAuthenticationDto load(String code) {
                        return UserAuthenticationDto.builder().build();
                    }
                });
    }

}