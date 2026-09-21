package com.demo.caching.service;

import com.demo.caching.entity.CachedMovie;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CachingMovieService {

    private int loads;

    @Cacheable(cacheNames = "movies", key = "#id")
    public CachedMovie findMovie(String id) {
        loads++;
        return new CachedMovie(id, "Sneakers");
    }

    @CachePut(cacheNames = "movies", key = "#movie.id")
    public CachedMovie updateMovie(CachedMovie movie) {
        return movie;
    }

    @CacheEvict(cacheNames = "movies", key = "#id")
    public void evictMovie(String id) {
    }

    public int loads() {
        return loads;
    }
}
