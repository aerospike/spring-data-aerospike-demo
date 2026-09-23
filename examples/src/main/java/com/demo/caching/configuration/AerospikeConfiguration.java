package com.demo.caching.configuration;

import com.aerospike.client.IAerospikeClient;
import com.demo.caching.entity.CacheEntryDocument;
import com.demo.caching.entity.CachedMovie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.cache.AerospikeCacheConfiguration;
import org.springframework.data.aerospike.cache.AerospikeCacheKeyProcessor;
import org.springframework.data.aerospike.cache.AerospikeCacheManager;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.convert.MappingAerospikeConverter;

@Configuration
@EnableCaching
public class AerospikeConfiguration extends AbstractAerospikeDataConfiguration {

    @Bean
    public CacheManager cacheManager(IAerospikeClient client, MappingAerospikeConverter converter,
                                     AerospikeCacheKeyProcessor cacheKeyProcessor,
                                     @Value("${spring.data.aerospike.namespace:test}") String namespace) {
        AerospikeCacheConfiguration cacheConfiguration =
            new AerospikeCacheConfiguration(namespace, CacheEntryDocument.SET_NAME);
        return new AerospikeCacheManager(client, converter, cacheConfiguration, cacheKeyProcessor);
    }

    @Override
    protected String getMappingBasePackage() {
        return CachedMovie.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setScansEnabled(true);
        aerospikeDataSettings.setCreateIndexesOnStartup(false);
    }
}
