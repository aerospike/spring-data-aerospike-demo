package com.demo.querymethods.repository;

import com.demo.querymethods.dto.MovieSummary;
import com.demo.querymethods.entity.QueryMethodsMovieDocument;
import org.springframework.data.aerospike.annotation.Query;
import org.springframework.data.aerospike.query.QueryParam;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface QueryMethodsMovieRepository extends AerospikeRepository<QueryMethodsMovieDocument, String> {

    List<QueryMethodsMovieDocument> findByGenre(String genre);

    List<QueryMethodsMovieDocument> findByReleaseYearBetween(int fromInclusive, int toInclusive);

    List<QueryMethodsMovieDocument> findByIdAndGenre(QueryParam ids, QueryParam genre);

    boolean existsByGenre(String genre);

    long countByReleaseYearBetween(int fromInclusive, int toInclusive);

    void deleteByGenre(String genre);

    @Query(
        expression = "$.releaseYear >= ?0 and $.releaseYear < ?1",
        indexToUse = QueryMethodsMovieDocument.RELEASE_YEAR_INDEX
    )
    List<QueryMethodsMovieDocument> findByReleaseYearBetweenUsingDeclaredQuery(int fromInclusive, int toExclusive);

    List<MovieSummary> findMovieSummaryById(String id);

    <T> List<T> findById(String id, Class<T> type);

    List<QueryMethodsMovieDocument> findByGenre(String genre, Sort sort);

    Page<QueryMethodsMovieDocument> findByReleaseYearLessThan(int releaseYear, Pageable pageable);

    Slice<QueryMethodsMovieDocument> findByReleaseYearGreaterThan(int releaseYear, Pageable pageable);

    Page<QueryMethodsMovieDocument> findAllById(Iterable<String> ids, Pageable pageable);
}
