package com.developer.onlybuns.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.CacheManager;
import javax.cache.spi.CachingProvider;
import java.net.URISyntaxException;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager jCacheManager() throws URISyntaxException {
        CachingProvider provider = Caching.getCachingProvider();
        return provider.getCacheManager(
                getClass().getResource("/ehcache.xml").toURI(),
                getClass().getClassLoader()
        );
    }
}

