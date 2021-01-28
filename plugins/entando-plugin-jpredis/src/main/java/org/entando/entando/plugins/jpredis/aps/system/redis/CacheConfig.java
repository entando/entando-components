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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author E.Santoboni
 */
//@Aspect
@Configuration
@ComponentScan
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    private static final String REDIS_PREFIX = "redis://";

    @Value("${REDIS_ADDRESS:redis://localhost:6379}")
    private String redisAddress;

    @Value("${REDIS_ADDRESSES:}")
    private String redisAddresses;

    @Value("${REDIS_MASTER_NAME:mymaster}")
    private String redisMasterName;

    @Value("${REDIS_PASSWORD:}")
    private String redisPassword;

    private CacheManager cacheManagerBean;

    private static RedisCacheConfiguration createCacheConfiguration(long timeoutInSeconds) {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(timeoutInSeconds));
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        if (!StringUtils.isBlank(this.redisAddresses)) {
            logger.warn("** Redis Cluster with sentinel configuration - the master node will be the first node defined in REDIS_ADDRESSES parameter **");
            String[] addresses = this.redisAddresses.split(",");
            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
            for (int i = 0; i < addresses.length; i++) {
                String address = addresses[i];
                String purgedAddress = (address.trim().startsWith(REDIS_PREFIX)) ? address.trim().substring(REDIS_PREFIX.length()) : address.trim();
                String[] sections = purgedAddress.split(":");
                RedisNode node = new RedisNode(sections[0], Integer.parseInt(sections[1]));
                if (i == 0) {
                    node.setName(this.redisMasterName);
                    sentinelConfig.setMaster(node);
                } else {
                    sentinelConfig.addSentinel(node);
                }
            }
            if (!StringUtils.isBlank(this.redisPassword)) {
                sentinelConfig.setPassword(this.redisPassword);
            }
            return new LettuceConnectionFactory(sentinelConfig);
        } else {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            String[] sections = this.redisAddress.substring(REDIS_PREFIX.length()).split(":");
            redisStandaloneConfiguration.setHostName(sections[0]);
            redisStandaloneConfiguration.setPort(Integer.parseInt(sections[1]));
            if (!StringUtils.isBlank(this.redisPassword)) {
                redisStandaloneConfiguration.setPassword(this.redisPassword);
            }
            return new LettuceConnectionFactory(redisStandaloneConfiguration);
        }
    }

    @Primary
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = this.buildDefaultConfiguration();
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        // time to leave = 4 Hours
        cacheConfigurations.put(ICacheInfoManager.DEFAULT_CACHE_NAME, createCacheConfiguration(4 * 60 * 60));
        CacheFrontend<String, Object> cacheFrontend = this.buildCacheFrontend();
        LettuceCacheManager manager = LettuceCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .cacheFrontend(cacheFrontend)
                .withInitialCacheConfigurations(cacheConfigurations).build();
        this.setCacheManagerBean(manager);
        return manager;
    }

    protected CacheFrontend<String, Object> buildCacheFrontend() {
        DefaultClientResources resources = DefaultClientResources.builder().build();
        StatefulRedisConnection<String, Object> myself = null;
        TrackingArgs trackingArgs = null;
        if (!StringUtils.isBlank(this.redisAddresses)) {
            logger.warn("** Client-side caching doesn't work on Redis Cluster and sharding data environments but only for Master/Slave environments (with sentinel) **");
            List<String> purgedAddresses = new ArrayList<>();
            String[] addresses = this.redisAddresses.split(",");
            for (int i = 0; i < addresses.length; i++) {
                String address = addresses[i];
                if (!address.trim().startsWith(REDIS_PREFIX)) {
                    purgedAddresses.add(address.trim());
                } else {
                    purgedAddresses.add(address.trim().substring(REDIS_PREFIX.length()));
                }
            }
            String[] sectionForMaster = purgedAddresses.get(0).split(":");
            RedisURI.Builder uriBuilder = RedisURI.Builder.sentinel(sectionForMaster[0], Integer.parseInt(sectionForMaster[1]), "mymaster");
            if (addresses.length > 1) {
                for (int i = 1; i < purgedAddresses.size(); i++) {
                    String[] sectionForSlave = purgedAddresses.get(i).split(":");
                    uriBuilder.withSentinel(sectionForSlave[0], Integer.parseInt(sectionForSlave[1]));
                }
            }
            RedisURI redisUri = uriBuilder.build();
            if (!StringUtils.isBlank(this.redisPassword)) {
                redisUri.setPassword(this.redisPassword.toCharArray());
            }
            RedisClient lettuceClient = new RedisClient(resources, redisUri) {
            };
            myself = lettuceClient.connect(new SerializedObjectCodec());
            trackingArgs = TrackingArgs.Builder.enabled().bcast();
        } else {
            RedisURI redisUri = RedisURI.create((this.redisAddress.startsWith(REDIS_PREFIX)) ? this.redisAddress : REDIS_PREFIX + this.redisAddress);
            if (!StringUtils.isBlank(this.redisPassword)) {
                redisUri.setPassword(this.redisPassword.toCharArray());
            }
            RedisClient lettuceClient = new RedisClient(resources, redisUri) {
            };
            myself = lettuceClient.connect(new SerializedObjectCodec());
            trackingArgs = TrackingArgs.Builder.enabled().noloop();
        }
        Map<String, Object> clientCache = new ConcurrentHashMap<>();
        CacheFrontend<String, Object> cacheFrontend = ClientSideCaching.enable(CacheAccessor.forMap(clientCache), myself, trackingArgs);
        return cacheFrontend;
    }
    /*
    @After("execution(* com.agiletec.apsadmin.admin.BaseAdminAction.reloadConfig())")
    public void executeReloadConfig(JoinPoint joinPoint) {
        CacheFrontend<String, Object> cacheFrontend = this.buildCacheFrontend();
        this.getCacheManagerBean().getCacheNames().stream().forEach(cacheName -> {
            LettuceCache cache = (LettuceCache) this.getCacheManagerBean().getCache(cacheName);
            cache.setCacheFrontend(cacheFrontend);
        });
    }
    */
    private RedisCacheConfiguration buildDefaultConfiguration() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ZERO);
        ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
                .failOnEmptyBeans(false)
                .build();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()));
        return config;
    }

    protected CacheManager getCacheManagerBean() {
        return cacheManagerBean;
    }

    protected void setCacheManagerBean(CacheManager cacheManagerBean) {
        this.cacheManagerBean = cacheManagerBean;
    }

}
