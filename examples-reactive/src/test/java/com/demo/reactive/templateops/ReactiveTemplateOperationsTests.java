package com.demo.reactive.templateops;

import com.demo.reactive.templateops.dto.ReactiveTemplateMovieSummary;
import com.demo.reactive.templateops.entity.ReactiveTemplateMovieDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;
import org.springframework.data.aerospike.query.FilterOperation;
import org.springframework.data.aerospike.query.qualifier.Qualifier;
import org.springframework.data.aerospike.repository.query.Query;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveTemplateOperationsTests extends ReactiveTemplateOperationsAerospikeDemoApplicationTest {

    @Autowired
    ReactiveAerospikeTemplate template;

    @BeforeEach
    void setUp() {
        template.deleteAll(ReactiveTemplateMovieDocument.class)
            .thenMany(template.insertAll(seedMovies()))
            .collectList()
            .block();
    }

    @Test
    void queryCountExistsAndProjectionUseCurrentTemplateOverloads() {
        Query scienceFiction = matchingGenre("science-fiction");

        StepVerifier.create(template.find(scienceFiction, ReactiveTemplateMovieDocument.class).collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveTemplateMovieDocument::getTitle)
                .containsExactlyInAnyOrder("Dark City", "The Matrix", "Arrival"))
            .verifyComplete();

        StepVerifier.create(template.exists(scienceFiction, ReactiveTemplateMovieDocument.class))
            .expectNext(true)
            .verifyComplete();

        StepVerifier.create(template.count(releasedBetween(1990, 2000), ReactiveTemplateMovieDocument.class))
            .expectNext(3L)
            .verifyComplete();

        StepVerifier.create(template
                .find(scienceFiction, ReactiveTemplateMovieDocument.class, ReactiveTemplateMovieSummary.class)
                .collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveTemplateMovieSummary::getTitle)
                .containsExactlyInAnyOrder("Dark City", "The Matrix", "Arrival"))
            .verifyComplete();
    }

    @Test
    void batchReadDeleteMutationsAndPartialUpdateAreAvailable() {
        StepVerifier.create(template.findByIds(
                List.of("reactive-template-1", "reactive-template-2"), ReactiveTemplateMovieDocument.class)
            .collectList())
            .assertNext(results -> assertThat(results)
                .extracting(ReactiveTemplateMovieDocument::getTitle)
                .containsExactlyInAnyOrder("Dark City", "The Matrix"))
            .verifyComplete();

        ReactiveTemplateMovieDocument matrix =
            new ReactiveTemplateMovieDocument("reactive-template-mutation", "trix", "science-fiction", 1999, 5, 10);
        template.insert(matrix).block();

        ReactiveTemplateMovieDocument viewed = template.add(matrix, "views", 5).block();
        ReactiveTemplateMovieDocument prefixed = template.prepend(viewed, "title", "The Ma").block();
        ReactiveTemplateMovieDocument renamed = template.append(prefixed, "title", " Reloaded").block();

        assertThat(viewed.getViews()).isEqualTo(15);
        assertThat(renamed.getTitle()).isEqualTo("The Matrix Reloaded");

        template.insert(new ReactiveTemplateMovieDocument(
            "reactive-template-partial", "Primer", "science-fiction", 2004, 4, 100)).block();
        template.update(new ReactiveTemplateMovieDocument(
            "reactive-template-partial", null, null, 0, 5, 0), List.of("rating")).block();

        StepVerifier.create(template.findById("reactive-template-partial", ReactiveTemplateMovieDocument.class))
            .assertNext(partial -> {
                assertThat(partial.getTitle()).isEqualTo("Primer");
                assertThat(partial.getRating()).isEqualTo(5);
            })
            .verifyComplete();

        template.deleteByIds(List.of("reactive-template-1", "reactive-template-2"),
            ReactiveTemplateMovieDocument.class).block();
        StepVerifier.create(template.exists("reactive-template-1", ReactiveTemplateMovieDocument.class))
            .expectNext(false)
            .verifyComplete();
    }

    private Query matchingGenre(String genre) {
        return new Query(Qualifier.builder()
            .setPath("genre")
            .setFilterOperation(FilterOperation.EQ)
            .setValue(genre)
            .build());
    }

    private Query releasedBetween(int fromInclusive, int toInclusive) {
        return new Query(Qualifier.builder()
            .setPath("releaseYear")
            .setFilterOperation(FilterOperation.BETWEEN)
            .setValue(fromInclusive)
            .setSecondValue(toInclusive)
            .build());
    }

    private List<ReactiveTemplateMovieDocument> seedMovies() {
        return List.of(
            new ReactiveTemplateMovieDocument("reactive-template-1", "Dark City", "science-fiction", 1998, 5, 20),
            new ReactiveTemplateMovieDocument("reactive-template-2", "The Matrix", "science-fiction", 1999, 5, 30),
            new ReactiveTemplateMovieDocument("reactive-template-3", "Heat", "crime", 1995, 5, 40),
            new ReactiveTemplateMovieDocument("reactive-template-4", "Arrival", "science-fiction", 2016, 5, 50)
        );
    }
}
