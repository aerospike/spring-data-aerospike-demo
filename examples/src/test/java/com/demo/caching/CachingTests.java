package com.demo.caching;

import com.demo.caching.entity.CacheEntryDocument;
import com.demo.caching.entity.CachedMovie;
import com.demo.caching.service.CachingMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class CachingTests extends CachingAerospikeDemoApplicationTest {

    @Autowired
    CachingMovieService service;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    AerospikeTemplate template;

    @BeforeEach
    void setUp() {
        template.deleteAll(CacheEntryDocument.class);
    }

    @Test
    void cacheAnnotationsUseAerospikeBackedCacheStorage() {
        int initialLoads = service.loads();

        CachedMovie first = service.findMovie("cache-movie-1");
        CachedMovie cached = service.findMovie("cache-movie-1");

        assertThat(cached.getTitle()).isEqualTo(first.getTitle());
        assertThat(service.loads()).isEqualTo(initialLoads + 1);

        service.updateMovie(new CachedMovie("cache-movie-1", "Sneakers Updated"));
        CachedMovie updated = service.findMovie("cache-movie-1");

        assertThat(updated.getTitle()).isEqualTo("Sneakers Updated");
        assertThat(service.loads()).isEqualTo(initialLoads + 1);

        service.evictMovie("cache-movie-1");
        CachedMovie reloaded = service.findMovie("cache-movie-1");

        assertThat(reloaded.getTitle()).isEqualTo("Sneakers");
        assertThat(service.loads()).isEqualTo(initialLoads + 2);
        assertThat(cache("movies").get("cache-movie-1", CachedMovie.class)).isNotNull();
    }

    private Cache cache(String name) {
        Cache cache = cacheManager.getCache(name);
        assertThat(cache).isNotNull();
        return cache;
    }
}
