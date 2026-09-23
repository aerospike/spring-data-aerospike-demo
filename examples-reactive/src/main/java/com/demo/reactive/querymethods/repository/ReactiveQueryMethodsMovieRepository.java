package com.demo.reactive.querymethods.repository;

import com.demo.reactive.querymethods.entity.ReactiveQueryMethodsMovieDocument;
import org.springframework.data.aerospike.annotation.Query;
import org.springframework.data.aerospike.query.QueryParam;
import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveQueryMethodsMovieRepository
    extends ReactiveAerospikeRepository<ReactiveQueryMethodsMovieDocument, String> {

    Flux<ReactiveQueryMethodsMovieDocument> findByGenre(String genre);

    Flux<ReactiveQueryMethodsMovieDocument> findByReleaseYearBetween(int fromInclusive, int toInclusive);

    Flux<ReactiveQueryMethodsMovieDocument> findByIdAndGenre(QueryParam ids, QueryParam genre);

    Mono<Boolean> existsByGenre(String genre);

    Mono<Long> countByReleaseYearBetween(int fromInclusive, int toInclusive);

    Mono<Void> deleteByGenre(String genre);

    @Query(
        expression = "$.releaseYear >= ?0 and $.releaseYear < ?1",
        indexToUse = ReactiveQueryMethodsMovieDocument.RELEASE_YEAR_INDEX
    )
    Flux<ReactiveQueryMethodsMovieDocument> findByReleaseYearBetweenUsingDeclaredQuery(int fromInclusive,
                                                                                      int toExclusive);
}
