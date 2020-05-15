/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jpredis.aps.system.redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author E.Santoboni
 */
@Configuration
@ComponentScan
@EnableCaching
public class RedisCacheClientApplication {
    
    @Value("${REDIS_ADDRESS:redis://localhost:6379}")
    private String redisAddress;

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        Config clientConfig = new Config();
        //clientConfig.useClusterServers().addNodeAddress("redis://127.0.0.1:6379");
        clientConfig.useSingleServer().setAddress(this.redisAddress);
        //config.useClusterServers()
        //        .addNodeAddress("redis://127.0.0.1:7004", "redis://127.0.0.1:7001");
        //clientConfig.setCodec(new CompositeCodec(new FstCodec(), new SerializationCodec(), new SerializationCodec()));
        clientConfig.setCodec(new SerializationCodec());
        return Redisson.create(clientConfig);
    }

    @Primary
    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> cachesConfig = new HashMap<>();
        // time to leave = 4 Hours
        // max idle = 30 minutes
        cachesConfig.put("Entando_Cache", new CacheConfig(4*60*60*1000, 30*60*1000));
        /*
        cachesConfig.put("Entando_PageManager", new CacheConfig());
        cachesConfig.put("Entando_PageModelManager", new CacheConfig());
        cachesConfig.put("Entando_WidgetTypeManager", new CacheConfig());
        cachesConfig.put("Entando_CategoryManager", new CacheConfig());
        cachesConfig.put("Entando_ConfigManager", new CacheConfig());
        cachesConfig.put("Entando_RoleManager", new CacheConfig());
        cachesConfig.put("Entando_GroupManager", new CacheConfig());
        cachesConfig.put("Entando_LangManager", new CacheConfig());
        cachesConfig.put("Entando_CacheInfoManager", new CacheConfig());
        cachesConfig.put("Entando_I18nManager", new CacheConfig());
        cachesConfig.put("Entando_LangManager", new CacheConfig());
        cachesConfig.put("Entando_UserProfileManager", new CacheConfig());
        cachesConfig.put("Entando_InitializerManager", new CacheConfig());
        cachesConfig.put("Entando_ApiCatalogManager", new CacheConfig());
        cachesConfig.put("Entando_KeyGeneratorManager", new CacheConfig());
        cachesConfig.put("Entando_DataObjectManager", new CacheConfig());
        cachesConfig.put("Entando_DataObjectModelManager", new CacheConfig());
        cachesConfig.put("Entando_DataObjectPageMapperManager", new CacheConfig());
        cachesConfig.put("Entando_KeyGeneratorManager", new CacheConfig());
        cachesConfig.put("Entando_KeyGeneratorManager", new CacheConfig());
        cachesConfig.put("Entando_KeyGeneratorManager", new CacheConfig());
        cachesConfig.put("Entando_KeyGeneratorManager", new CacheConfig());
        cachesConfig.put("Entando_RemoteBeanCatalogManager", new CacheConfig());
        */
        RedissonSpringCacheManager manager = new RedissonSpringCacheManager(redissonClient, cachesConfig, new SerializationCodec());
        manager.setAllowNullValues(true);
        return manager;
    }

}
