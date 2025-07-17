
/*package com.developer.onlybuns.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.CacheManager;
import javax.cache.spi.CachingProvider;
import java.net.URISyntaxException;

@Configuration
public class CacheConfig {

    @Value("${spring.cache.jcache.config}")
    private String configFile;

    @Bean
    public CacheManager jCacheManager() throws URISyntaxException {
        return Caching.getCachingProvider().getCacheManager(
        getClass().getResource(configFile).toURI(),
        getClass().getClassLoader()
    );
}

}
*/

/*
package com.developer.onlybuns.config;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import java.net.URISyntaxException;

@Configuration
public class CacheConfig {
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public CacheManager jCacheManager() throws URISyntaxException {
        CachingProvider provider = Caching.getCachingProvider();
        String configFile = "test".equals(activeProfile)
                ? "/ehcache-test.xml"
                : "/ehcache.xml";
        return provider.getCacheManager(
                getClass().getResource(configFile).toURI(),
                getClass().getClassLoader()
        );
    }
}
*/

package com.developer.onlybuns.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URI;

@Configuration
public class CacheConfig {

    @Value("${spring.cache.jcache.config}")
    private String configFile;

    private final ResourceLoader resourceLoader;

    public CacheConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public CacheManager jCacheManager() throws Exception {
        Resource resource = resourceLoader.getResource(configFile);
        URI uri = resource.getURI();
        CachingProvider provider = Caching.getCachingProvider();
        return provider.getCacheManager(uri, getClass().getClassLoader());
    }
}
