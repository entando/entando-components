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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.cache.CacheManager} backed by a {@link RedisCache Redis} cache.
 * <p />
 * Rewriting of default implementation of redis manager {@link org.springframework.data.redis.cache.RedisCacheManager} 
 * for custom implementation. This custom extension allows the manager to create a custom Cache (LettuceCache) with 
 * Client-side caching support (provided by CacheFrontend instance).
 * This cache manager creates caches by default upon first write. Empty caches are not visible on Redis due to how Redis
 * represents empty data structures.
 * <p />
 * Caches requiring a different {@link RedisCacheConfiguration} than the default configuration can be specified via
 * {@link RedisCacheManagerBuilder#withInitialCacheConfigurations(Map)}.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 * @see RedisCacheConfiguration
 * @see RedisCacheWriter
 */
public class LettuceCacheManager extends AbstractTransactionSupportingCacheManager {

	private final RedisCacheWriter cacheWriter;
	private final RedisCacheConfiguration defaultCacheConfig;
	private final Map<String, RedisCacheConfiguration> initialCacheConfiguration;
	private final boolean allowInFlightCacheCreation;
    private CacheFrontend<String, Object> cacheFrontend;

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param allowInFlightCacheCreation allow create unconfigured caches.
     * @param cacheFrontend Allow to add Client-side caching support
	 * @since 2.0.4
	 */
	private LettuceCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
			boolean allowInFlightCacheCreation, CacheFrontend<String, Object> cacheFrontend) {
		Assert.notNull(cacheWriter, "CacheWriter must not be null!");
		Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");
		this.cacheWriter = cacheWriter;
		this.defaultCacheConfig = defaultCacheConfiguration;
		this.initialCacheConfiguration = new LinkedHashMap<>();
		this.allowInFlightCacheCreation = allowInFlightCacheCreation;
        this.cacheFrontend = cacheFrontend;
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 */
	public LettuceCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
		this(cacheWriter, defaultCacheConfiguration, true, null);
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param initialCacheNames optional set of known cache names that will be created with given
	 *          {@literal defaultCacheConfiguration}.
	 */
	public LettuceCacheManager(RedisCacheWriter cacheWriter, 
            RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
		this(cacheWriter, defaultCacheConfiguration, true, null, initialCacheNames);
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param allowInFlightCacheCreation if set to {@literal true} no new caches can be acquire at runtime but limited to
	 *          the given list of initial cache names.
     * @param cacheFrontend Allow to add Client-side caching support
	 * @param initialCacheNames optional set of known cache names that will be created with given
	 *          {@literal defaultCacheConfiguration}.
	 * @since 2.0.4
	 */
	public LettuceCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
			boolean allowInFlightCacheCreation, CacheFrontend<String, Object> cacheFrontend, String... initialCacheNames) {
		this(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, cacheFrontend);
		for (String cacheName : initialCacheNames) {
			this.initialCacheConfiguration.put(cacheName, defaultCacheConfiguration);
		}
	}
    
	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
	 *          Must not be {@literal null}.
     * @param cacheFrontend Allow to add Client-side caching support
	 */
	public LettuceCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
			Map<String, RedisCacheConfiguration> initialCacheConfigurations, CacheFrontend<String, Object> cacheFrontend) {
		this(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, true, cacheFrontend);
	}

	/**
	 * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
	 * {@link RedisCacheConfiguration}.
	 *
	 * @param cacheWriter must not be {@literal null}.
	 * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
	 *          {@link RedisCacheConfiguration#defaultCacheConfig()}.
	 * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
	 *          Must not be {@literal null}.
	 * @param allowInFlightCacheCreation if set to {@literal false} this cache manager is limited to the initial cache
	 *          configurations and will not create new caches at runtime.
     * @param cacheFrontend Allow to add Client-side caching support
	 * @since 2.0.4
	 */
	public LettuceCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
			Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation, CacheFrontend<String, Object> cacheFrontend) {
		this(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, cacheFrontend);
		Assert.notNull(initialCacheConfigurations, "InitialCacheConfigurations must not be null!");
		this.initialCacheConfiguration.putAll(initialCacheConfigurations);
	}

	/**
	 * Create a new {@link RedisCacheManager} with defaults applied.
	 * <dl>
	 * <dt>locking</dt>
	 * <dd>disabled</dd>
	 * <dt>cache configuration</dt>
	 * <dd>{@link RedisCacheConfiguration#defaultCacheConfig()}</dd>
	 * <dt>initial caches</dt>
	 * <dd>none</dd>
	 * <dt>transaction aware</dt>
	 * <dd>no</dd>
	 * <dt>in-flight cache creation</dt>
	 * <dd>enabled</dd>
	 * </dl>
	 *
	 * @param connectionFactory must not be {@literal null}.
	 * @return new instance of {@link RedisCacheManager}.
	 */
	public static LettuceCacheManager create(RedisConnectionFactory connectionFactory) {
		Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        DefaultLettuceCacheWriter writer = new DefaultLettuceCacheWriter(connectionFactory);
		return new LettuceCacheManager(writer, RedisCacheConfiguration.defaultCacheConfig());
	}

	/**
	 * Entry point for builder style {@link RedisCacheManager} configuration.
	 *
	 * @param connectionFactory must not be {@literal null}.
	 * @return new {@link RedisCacheManagerBuilder}.
	 */
	public static RedisCacheManagerBuilder builder(RedisConnectionFactory connectionFactory) {
		Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
		return RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory);
	}

	/**
	 * Entry point for builder style {@link RedisCacheManager} configuration.
	 *
	 * @param cacheWriter must not be {@literal null}.
	 * @return new {@link RedisCacheManagerBuilder}.
	 */
	public static RedisCacheManagerBuilder builder(RedisCacheWriter cacheWriter) {
		Assert.notNull(cacheWriter, "CacheWriter must not be null!");
		return RedisCacheManagerBuilder.fromCacheWriter(cacheWriter);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.cache.support.AbstractCacheManager#loadCaches()
	 */
	@Override
	protected Collection<RedisCache> loadCaches() {
		List<RedisCache> caches = new LinkedList<>();
		for (Map.Entry<String, RedisCacheConfiguration> entry : initialCacheConfiguration.entrySet()) {
			caches.add(createRedisCache(entry.getKey(), entry.getValue()));
		}
		return caches;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.cache.support.AbstractCacheManager#getMissingCache(java.lang.String)
	 */
	@Override
	protected RedisCache getMissingCache(String name) {
		return allowInFlightCacheCreation ? createRedisCache(name, defaultCacheConfig) : null;
	}

	/**
	 * @return unmodifiable {@link Map} containing cache name / configuration pairs. Never {@literal null}.
	 */
	public Map<String, RedisCacheConfiguration> getCacheConfigurations() {
		Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(getCacheNames().size());
		getCacheNames().forEach(it -> {
			RedisCache cache = RedisCache.class.cast(lookupCache(it));
			configurationMap.put(it, cache != null ? cache.getCacheConfiguration() : null);
		});
		return Collections.unmodifiableMap(configurationMap);
	}

	/**
	 * Configuration hook for creating {@link RedisCache} with given name and {@code cacheConfig}.
	 *
	 * @param name must not be {@literal null}.
	 * @param cacheConfig can be {@literal null}.
	 * @return never {@literal null}.
	 */
	protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
		return new LettuceCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig, this.cacheFrontend);
	}

	/**
	 * Configurator for creating {@link RedisCacheManager}.
	 *
	 * @author Christoph Strobl
	 * @author Mark Strobl
	 * @author Kezhu Wang
	 * @since 2.0
	 */
	public static class RedisCacheManagerBuilder {
        
		private final RedisCacheWriter cacheWriter;
		private RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
		private final Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
		private boolean enableTransactions;
		boolean allowInFlightCacheCreation = true;
        private CacheFrontend<String, Object> cacheFrontend;
        
		private RedisCacheManagerBuilder(RedisCacheWriter cacheWriter) {
			this.cacheWriter = cacheWriter;
		}

		/**
		 * Entry point for builder style {@link RedisCacheManager} configuration.
		 *
		 * @param connectionFactory must not be {@literal null}.
		 * @return new {@link RedisCacheManagerBuilder}.
		 */
		public static RedisCacheManagerBuilder fromConnectionFactory(RedisConnectionFactory connectionFactory) {
			Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
            return builder(new DefaultLettuceCacheWriter(connectionFactory));
		}

		/**
		 * Entry point for builder style {@link RedisCacheManager} configuration.
		 *
		 * @param cacheWriter must not be {@literal null}.
		 * @return new {@link RedisCacheManagerBuilder}.
		 */
		public static RedisCacheManagerBuilder fromCacheWriter(RedisCacheWriter cacheWriter) {
			Assert.notNull(cacheWriter, "CacheWriter must not be null!");
			return new RedisCacheManagerBuilder(cacheWriter);
		}

		/**
		 * Define a default {@link RedisCacheConfiguration} applied to dynamically created {@link RedisCache}s.
		 *
		 * @param defaultCacheConfiguration must not be {@literal null}.
		 * @return this {@link RedisCacheManagerBuilder}.
		 */
		public RedisCacheManagerBuilder cacheDefaults(RedisCacheConfiguration defaultCacheConfiguration) {
			Assert.notNull(defaultCacheConfiguration, "DefaultCacheConfiguration must not be null!");
			this.defaultCacheConfiguration = defaultCacheConfiguration;
			return this;
		}

		/**
		 * Enable {@link RedisCache}s to synchronize cache put/evict operations with ongoing Spring-managed transactions.
		 *
		 * @return this {@link RedisCacheManagerBuilder}.
		 */
		public RedisCacheManagerBuilder transactionAware() {
			this.enableTransactions = true;
			return this;
		}

		/**
		 * Append a {@link Set} of cache names to be pre initialized with current {@link RedisCacheConfiguration}.
		 * <strong>NOTE:</strong> This calls depends on {@link #cacheDefaults(RedisCacheConfiguration)} using whatever
		 * default {@link RedisCacheConfiguration} is present at the time of invoking this method.
		 *
		 * @param cacheNames must not be {@literal null}.
		 * @return this {@link RedisCacheManagerBuilder}.
		 */
		public RedisCacheManagerBuilder initialCacheNames(Set<String> cacheNames) {
			Assert.notNull(cacheNames, "CacheNames must not be null!");
			Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(cacheNames.size());
			cacheNames.forEach(it -> cacheConfigMap.put(it, defaultCacheConfiguration));
			return withInitialCacheConfigurations(cacheConfigMap);
		}

		/**
		 * Append a {@link Map} of cache name/{@link RedisCacheConfiguration} pairs to be pre initialized.
		 *
		 * @param cacheConfigurations must not be {@literal null}.
		 * @return this {@link RedisCacheManagerBuilder}.
		 */
		public RedisCacheManagerBuilder withInitialCacheConfigurations(Map<String, RedisCacheConfiguration> cacheConfigurations) {
			Assert.notNull(cacheConfigurations, "CacheConfigurations must not be null!");
			cacheConfigurations.forEach((cacheName, configuration) -> Assert.notNull(configuration,
					String.format("RedisCacheConfiguration for cache %s must not be null!", cacheName)));
			this.initialCaches.putAll(cacheConfigurations);
			return this;
		}

        public RedisCacheManagerBuilder cacheFrontend(CacheFrontend<String, Object> cacheFrontend) {
            this.cacheFrontend = cacheFrontend;
            return this;
        }

		/**
		 * Disable in-flight {@link org.springframework.cache.Cache} creation for unconfigured caches.
		 * <p />
		 * {@link RedisCacheManager#getMissingCache(String)} returns {@literal null} for any unconfigured
		 * {@link org.springframework.cache.Cache} instead of a new {@link RedisCache} instance. This allows eg.
		 * {@link org.springframework.cache.support.CompositeCacheManager} to chime in.
		 *
		 * @return this {@link RedisCacheManagerBuilder}.
		 * @since 2.0.4
		 */
		public RedisCacheManagerBuilder disableCreateOnMissingCache() {
			this.allowInFlightCacheCreation = false;
			return this;
		}

		/**
		 * Create new instance of {@link RedisCacheManager} with configuration options applied.
		 *
		 * @return new instance of {@link RedisCacheManager}.
		 */
		public LettuceCacheManager build() {
			LettuceCacheManager cm = new LettuceCacheManager(cacheWriter, 
                    defaultCacheConfiguration, initialCaches, allowInFlightCacheCreation, cacheFrontend);
			cm.setTransactionAware(enableTransactions);
			return cm;
		}
        
	}
    
}
