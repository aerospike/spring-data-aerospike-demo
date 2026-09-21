package com.demo.reactive.querymethods;

import com.demo.reactive.querymethods.dto.ReactiveMovieSummary;
import com.demo.reactive.querymethods.entity.ReactiveQueryMethodsMovieDocument;
import com.demo.reactive.querymethods.repository.ReactiveQueryMethodsMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;
import org.springframework.data.aerospike.query.FilterOperation;
import org.springframework.data.aerospike.query.QueryParam;
import org.springframework.data.aerospike.query.qualifier.Qualifier;
import org.springframework.data.aerospike.repository.query.Query;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveQueryMethodsRepositoryTests extends ReactiveQueryMethodsAerospikeDemoApplicationTest {

    @Autowired
    ReactiveQueryMethodsMovieRepository repository;

    @Autowired
    ReactiveAerospikeTemplate template;

    @BeforeEach
    void setUp() {
        template.deleteAll(ReactiveQueryMethodsMovieDocument.class)
            .thenMany(repository.saveAll(seedMovies()))
            .collectList()
            .block();
        template.refreshIndexesCache().block();
    }

    @Test
    void derivedQueries_findExistCountAndDeleteUsingSecondaryIndexes() {
        StepVerifier.create(repository.findByGenre("science-fiction").collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveQueryMethodsMovieDocument::getTitle)
                .containsExactlyInAnyOrder("Dark City", "The Matrix", "Moon", "Arrival"))
            .verifyComplete();

        StepVerifier.create(repository.findByReleaseYearBetween(1990, 2000).collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveQueryMethodsMovieDocument::getTitle)
                .containsExactlyInAnyOrder("Dark City", "The Matrix", "Heat"))
            .verifyComplete();

        StepVerifier.create(repository.existsByGenre("crime"))
            .expectNext(true)
            .verifyComplete();

        StepVerifier.create(repository.countByReleaseYearBetween(1990, 2000))
            .expectNext(3L)
            .verifyComplete();

        StepVerifier.create(repository.deleteByGenre("crime").then(repository.existsByGenre("crime")))
            .expectNext(false)
            .verifyComplete();
    }

    @Test
    void derivedQuery_combinesIdAndBinCriteriaWithQueryParam() {
        StepVerifier.create(repository.findByIdAndGenre(
                QueryParam.of("reactive-query-methods-1", "reactive-query-methods-3"),
                QueryParam.of("science-fiction")
            ).collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveQueryMethodsMovieDocument::getTitle)
                .containsExactly("Dark City"))
            .verifyComplete();
    }

    @Test
    void declaredDslQuery_usesExplicitIndex() {
        StepVerifier.create(repository.findByReleaseYearBetweenUsingDeclaredQuery(2000, 2010).collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveQueryMethodsMovieDocument::getTitle)
                .containsExactly("Moon"))
            .verifyComplete();
    }

    @Test
    void customQuery_usesQualifierAndProjectionTargetClass() {
        Query releasedNinetiesScienceFiction = new Query(Qualifier.and(
            Qualifier.builder()
                .setPath("genre")
                .setFilterOperation(FilterOperation.EQ)
                .setValue("science-fiction")
                .build(),
            Qualifier.builder()
                .setPath("releaseYear")
                .setFilterOperation(FilterOperation.BETWEEN)
                .setValue(1990)
                .setSecondValue(2000)
                .build()
        ));

        StepVerifier.create(repository.findUsingQuery(releasedNinetiesScienceFiction, ReactiveMovieSummary.class)
                .collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveMovieSummary::getTitle)
                .containsExactlyInAnyOrder("Dark City", "The Matrix"))
            .verifyComplete();
    }

    private List<ReactiveQueryMethodsMovieDocument> seedMovies() {
        return List.of(
            new ReactiveQueryMethodsMovieDocument("reactive-query-methods-1", "Dark City", "science-fiction", 1998),
            new ReactiveQueryMethodsMovieDocument("reactive-query-methods-2", "The Matrix", "science-fiction", 1999),
            new ReactiveQueryMethodsMovieDocument("reactive-query-methods-3", "Heat", "crime", 1995),
            new ReactiveQueryMethodsMovieDocument("reactive-query-methods-4", "Moon", "science-fiction", 2009),
            new ReactiveQueryMethodsMovieDocument("reactive-query-methods-5", "Arrival", "science-fiction", 2016)
        );
    }
}
