package com.example.expensetracker.config;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.CacheManager;

@Configuration
public class RateLimitingConfig {
    // cache manager is used by rate limiter to store the request count based on IP Address, how this works?
    // CaffeineCachingProvider helps to create a cache manager which helps create a cache named "rate-limit-cache",
    // lets say any user with specific IP Address makes request, the rate limiter checks this cache to understand
    // number of requests made by user in specific time frame, if exceeds limit, further requests are blocked.
    @Bean
    public CacheManager jcacheManager() {
        CaffeineCachingProvider caffeineCachingProvider = new CaffeineCachingProvider();
        CacheManager cacheManager = caffeineCachingProvider.getCacheManager(
                caffeineCachingProvider.getDefaultURI(),
                null
        );
        MutableConfiguration<String, Object> cacheConfig = new MutableConfiguration<>();
        cacheConfig.setStoreByValue(false);
        cacheManager.createCache("rate-limit-cache", cacheConfig);
        return cacheManager;
    }
}
