/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpredis.aps.system.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TrackingArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.support.caching.CacheAccessor;
import io.lettuce.core.support.caching.CacheFrontend;
import io.lettuce.core.support.caching.ClientSideCaching;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author E.Santoboni
 */
@Configuration
@ComponentScan
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    
    @Value("${REDIS_ADDRESS:redis://localhost:6379}")
    private String redisAddress;
    
    @Value("${REDIS_PASSWORD:}")
    private String redisPassword;
    
    private static RedisCacheConfiguration createCacheConfiguration(long timeoutInSeconds) {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(timeoutInSeconds));
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        String[] sections = this.redisAddress.substring("redis://".length()).split(":");
        redisStandaloneConfiguration.setHostName(sections[0]);
        redisStandaloneConfiguration.setPort(Integer.parseInt(sections[1]));
        if (!StringUtils.isBlank(this.redisPassword)) {
            redisStandaloneConfiguration.setPassword(this.redisPassword);
        }
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
    
    @Primary
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = this.buildDefaultConfiguration();
        
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // time to leave = 4 Hours
        cacheConfigurations.put(ICacheInfoManager.DEFAULT_CACHE_NAME, createCacheConfiguration(4*60*60));;
        
        RedisURI redisUri = RedisURI.create(this.redisAddress);
        if (!StringUtils.isBlank(this.redisPassword)) {
            redisUri.setPassword(this.redisPassword.toCharArray());
        }
        
        DefaultClientResources resources = DefaultClientResources.builder().build();
        RedisClient lettuceClient = new RedisClient(resources, redisUri){};
        
        Map<String, Object> clientCache = new ConcurrentHashMap<>();
        StatefulRedisConnection<String, Object> myself = lettuceClient.connect(new SerializedObjectCodec());
        CacheFrontend<String, Object> cacheFrontend = ClientSideCaching.enable(CacheAccessor.forMap(clientCache), myself, TrackingArgs.Builder.enabled().noloop());
        
        LettuceCacheManager manager = LettuceCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .cacheFrontend(cacheFrontend)
                .withInitialCacheConfigurations(cacheConfigurations).build();
        return manager;
    }
    
    private RedisCacheConfiguration buildDefaultConfiguration() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ZERO);
        ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
                .failOnEmptyBeans(false)
                .build();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()));
        return config;
    }
    
}
