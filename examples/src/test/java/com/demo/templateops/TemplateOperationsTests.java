package com.demo.templateops;

import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.demo.templateops.dto.TemplateMovieSummary;
import com.demo.templateops.entity.TemplateMovieDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.aerospike.core.AerospikeTemplate;
import org.springframework.data.aerospike.core.WritePolicyBuilder;
import org.springframework.data.aerospike.query.FilterOperation;
import org.springframework.data.aerospike.query.qualifier.Qualifier;
import org.springframework.data.aerospike.repository.query.Query;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateOperationsTests extends TemplateOperationsAerospikeDemoApplicationTest {

    @Autowired
    AerospikeTemplate template;

    @BeforeEach
    void setUp() {
        template.deleteAll(TemplateMovieDocument.class);
        template.insertAll(seedMovies());
    }

    @Test
    void queryCountExistsAndProjectionUseCurrentTemplateOverloads() {
        Query scienceFiction = matchingGenre("science-fiction");

        assertThat(template.find(scienceFiction, TemplateMovieDocument.class).toList())
            .extracting(TemplateMovieDocument::getTitle)
            .containsExactlyInAnyOrder("Dark City", "The Matrix", "Arrival");

        assertThat(template.exists(scienceFiction, TemplateMovieDocument.class)).isTrue();
        assertThat(template.count(releasedBetween(1990, 2000), TemplateMovieDocument.class)).isEqualTo(3);

        Stream<TemplateMovieSummary> summaries =
            template.find(scienceFiction, TemplateMovieDocument.class, TemplateMovieSummary.class);
        assertThat(summaries.sorted(Comparator.comparingInt(TemplateMovieSummary::getReleaseYear)).toList())
            .extracting(TemplateMovieSummary::getTitle)
            .containsExactly("Dark City", "The Matrix", "Arrival");
    }

    @Test
    void batchReadDeleteMutationsPartialUpdateAndCustomPolicyAreAvailable() {
        assertThat(template.findByIds(
            List.of("template-1", "template-2"), TemplateMovieDocument.class))
            .extracting(TemplateMovieDocument::getTitle)
            .containsExactlyInAnyOrder("Dark City", "The Matrix");

        TemplateMovieDocument matrix =
            new TemplateMovieDocument("template-mutation", "trix", "science-fiction", 1999, 5, 10);
        template.insert(matrix);

        TemplateMovieDocument viewed = template.add(matrix, "views", 5);
        TemplateMovieDocument prefixed = template.prepend(viewed, "title", "The Ma");
        TemplateMovieDocument renamed = template.append(prefixed, "title", " Reloaded");

        assertThat(viewed.getViews()).isEqualTo(15);
        assertThat(renamed.getTitle()).isEqualTo("The Matrix Reloaded");

        template.insert(new TemplateMovieDocument("template-partial", "Primer", "science-fiction", 2004, 4, 100));
        template.update(new TemplateMovieDocument("template-partial", null, null, 0, 5, 0), List.of("rating"));
        TemplateMovieDocument partial = template.findById("template-partial", TemplateMovieDocument.class);
        assertThat(partial.getTitle()).isEqualTo("Primer");
        assertThat(partial.getRating()).isEqualTo(5);

        WritePolicy createOnly = WritePolicyBuilder.builder(template.getAerospikeClient().getWritePolicyDefault())
            .recordExistsAction(RecordExistsAction.CREATE_ONLY)
            .build();
        template.persist(new TemplateMovieDocument("template-policy", "Moon", "science-fiction", 2009, 5, 60),
            createOnly);
        assertThat(template.exists("template-policy", TemplateMovieDocument.class)).isTrue();

        template.deleteByIds(List.of("template-1", "template-2"), TemplateMovieDocument.class);
        assertThat(template.exists("template-1", TemplateMovieDocument.class)).isFalse();
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

    private List<TemplateMovieDocument> seedMovies() {
        return List.of(
            new TemplateMovieDocument("template-1", "Dark City", "science-fiction", 1998, 5, 20),
            new TemplateMovieDocument("template-2", "The Matrix", "science-fiction", 1999, 5, 30),
            new TemplateMovieDocument("template-3", "Heat", "crime", 1995, 5, 40),
            new TemplateMovieDocument("template-4", "Arrival", "science-fiction", 2016, 5, 50)
        );
    }
}
