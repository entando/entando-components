/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpredis.aps.system.redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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
    
    @Value("${REDIS_USERNAME:}")
    private String redisUsername;
    
    @Value("${REDIS_PASSWORD:}")
    private String redisPassword;
    
    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        Config clientConfig = new Config();
        clientConfig.useSingleServer().setAddress(this.redisAddress)
                .setUsername((StringUtils.isBlank(this.redisUsername) ? null : this.redisUsername))
                .setPassword((StringUtils.isBlank(this.redisPassword) ? null : this.redisPassword));
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
        RedissonSpringCacheManager manager = new RedissonSpringCacheManager(redissonClient, cachesConfig, new SerializationCodec());
        manager.setAllowNullValues(true);
        return manager;
    }

}
