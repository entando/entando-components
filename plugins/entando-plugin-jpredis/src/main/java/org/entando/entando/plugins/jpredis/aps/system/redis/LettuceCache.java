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

import io.lettuce.core.support.caching.CacheFrontend;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

/**
 * Extension of default RedisCache with Client-side caching support
 * @author E.Santoboni
 */
public class LettuceCache extends RedisCache {
    
    private static final Logger logger = LoggerFactory.getLogger(LettuceCache.class);
    
    private CacheFrontend<String, Object> frontendCache;
    
	protected LettuceCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, CacheFrontend<String, Object> cacheFrontend) {
		super(name, cacheWriter, cacheConfig);
        this.frontendCache = cacheFrontend;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, @Nullable Class<T> type) {
        Object value = super.fromStoreValue(this.getFromLocalCache(key));
        if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException(
					"Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		return (T) value;
    }
    
    @Override
    public ValueWrapper get(Object key) {
        Object localCacheValue = this.getFromLocalCache(key);
        return toValueWrapper(localCacheValue);
    }
    
    private Object getFromLocalCache(Object key) {
        return this.frontendCache.get(super.getName() + "::" + key.toString());
    }
    
    @Override
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        logger.warn("Calling remote cache for key {}", key);
        return super.get(key, valueLoader);
    }
    
}
