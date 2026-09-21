package com.demo.querymethods;

import com.demo.querymethods.dto.MovieSummary;
import com.demo.querymethods.entity.QueryMethodsMovieDocument;
import com.demo.querymethods.repository.QueryMethodsMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.aerospike.core.AerospikeTemplate;
import org.springframework.data.aerospike.query.FilterOperation;
import org.springframework.data.aerospike.query.QueryParam;
import org.springframework.data.aerospike.query.qualifier.Qualifier;
import org.springframework.data.aerospike.repository.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryMethodsRepositoryTests extends QueryMethodsAerospikeDemoApplicationTest {

    @Autowired
    QueryMethodsMovieRepository repository;

    @Autowired
    AerospikeTemplate template;

    @BeforeEach
    void setUp() {
        template.deleteAll(QueryMethodsMovieDocument.class);
        repository.saveAll(seedMovies());
        template.refreshIndexesCache();
    }

    @Test
    void derivedQueries_findExistCountAndDeleteUsingSecondaryIndexes() {
        assertThat(repository.findByGenre("science-fiction"))
            .extracting(QueryMethodsMovieDocument::getTitle)
            .containsExactlyInAnyOrder("Dark City", "The Matrix", "Moon", "Arrival");

        assertThat(repository.findByReleaseYearBetween(1990, 2000))
            .extracting(QueryMethodsMovieDocument::getTitle)
            .containsExactlyInAnyOrder("Dark City", "The Matrix", "Heat");

        assertThat(repository.existsByGenre("crime")).isTrue();
        assertThat(repository.countByReleaseYearBetween(1990, 2000)).isEqualTo(3);

        repository.deleteByGenre("crime");
        assertThat(repository.existsByGenre("crime")).isFalse();
    }

    @Test
    void derivedQuery_combinesIdAndBinCriteriaWithQueryParam() {
        List<QueryMethodsMovieDocument> results = repository.findByIdAndGenre(
            QueryParam.of("query-methods-1", "query-methods-3"),
            QueryParam.of("science-fiction")
        );

        assertThat(results)
            .extracting(QueryMethodsMovieDocument::getTitle)
            .containsExactly("Dark City");
    }

    @Test
    void declaredDslQuery_usesExplicitIndex() {
        assertThat(repository.findByReleaseYearBetweenUsingDeclaredQuery(2000, 2010))
            .extracting(QueryMethodsMovieDocument::getTitle)
            .containsExactly("Moon");
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

        Iterable<MovieSummary> summaries = repository.findUsingQuery(releasedNinetiesScienceFiction, MovieSummary.class);

        assertThat(StreamSupport.stream(summaries.spliterator(), false).toList())
            .extracting(MovieSummary::getTitle)
            .containsExactlyInAnyOrder("Dark City", "The Matrix");
    }

    @Test
    void projectionsAndPagination_useRepositoryOverloads() {
        assertThat(repository.findMovieSummaryById("query-methods-1"))
            .singleElement()
            .extracting(MovieSummary::getTitle)
            .isEqualTo("Dark City");

        assertThat(repository.findById("query-methods-2", MovieSummary.class))
            .singleElement()
            .extracting(MovieSummary::getReleaseYear)
            .isEqualTo(1999);

        assertThat(repository.findByGenre("science-fiction", Sort.by("releaseYear")))
            .extracting(QueryMethodsMovieDocument::getTitle)
            .containsExactly("Dark City", "The Matrix", "Moon", "Arrival");

        Page<QueryMethodsMovieDocument> page = repository.findByReleaseYearLessThan(
            2010, PageRequest.of(0, 2, Sort.by("releaseYear")));
        assertThat(page.getNumberOfElements()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(4);

        Slice<QueryMethodsMovieDocument> slice = repository.findByReleaseYearGreaterThan(
            1990, PageRequest.of(0, 2, Sort.by("releaseYear")));
        assertThat(slice.hasNext()).isTrue();

        Page<QueryMethodsMovieDocument> idPage = repository.findAllById(
            List.of("query-methods-1", "query-methods-2", "query-methods-3", "query-methods-4"),
            PageRequest.of(1, 2));
        assertThat(idPage.getContent())
            .extracting(QueryMethodsMovieDocument::getId)
            .containsExactly("query-methods-3", "query-methods-4");
    }

    private List<QueryMethodsMovieDocument> seedMovies() {
        return List.of(
            new QueryMethodsMovieDocument("query-methods-1", "Dark City", "science-fiction", 1998),
            new QueryMethodsMovieDocument("query-methods-2", "The Matrix", "science-fiction", 1999),
            new QueryMethodsMovieDocument("query-methods-3", "Heat", "crime", 1995),
            new QueryMethodsMovieDocument("query-methods-4", "Moon", "science-fiction", 2009),
            new QueryMethodsMovieDocument("query-methods-5", "Arrival", "science-fiction", 2016)
        );
    }
}
