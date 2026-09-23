package com.demo.springbootautoconfigure;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import com.demo.springbootautoconfigure.repository.AutoconfiguredMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoconfiguredMovieRepositoryTests extends SpringBootAutoconfigureAerospikeDemoApplicationTest {

    String id;
    AutoconfiguredMovieDocument movie;

    @Autowired
    AutoconfiguredMovieRepository repository;

    @Autowired
    AerospikeTemplate template;

    @BeforeEach
    void setUp() {
        template.deleteAll(AutoconfiguredMovieDocument.class);
        id = UUID.randomUUID().toString();
        movie = AutoconfiguredMovieDocument.builder()
                .id(id)
                .title("The Iron Giant")
                .director("Brad Bird")
                .releaseYear(1999)
                .build();
    }

    @Test
    public void saveMovie() {
        repository.save(movie);
        assertThat(repository.findById(id)).hasValue(movie);
    }

    @Test
    public void exists_returnsTrueIfMovieIsPresent() {
        repository.save(movie);
        assertThat(repository.existsById(id)).isTrue();
    }

    @Test
    public void deleteExistingMovieById() {
        repository.save(movie);
        repository.deleteById(id);
        assertThat(repository.findById(id)).isNotPresent();
    }

    @Test
    void findByDirector_returnsMoviesFromIndexedDirectorQuery() {
        AutoconfiguredMovieDocument secondBradBirdMovie = AutoconfiguredMovieDocument.builder()
                .id(UUID.randomUUID().toString())
                .title("Ratatouille")
                .director("Brad Bird")
                .releaseYear(2007)
                .build();
        AutoconfiguredMovieDocument otherDirectorMovie = AutoconfiguredMovieDocument.builder()
                .id(UUID.randomUUID().toString())
                .title("Spirited Away")
                .director("Hayao Miyazaki")
                .releaseYear(2001)
                .build();

        repository.saveAll(List.of(movie, secondBradBirdMovie, otherDirectorMovie));
        template.refreshIndexesCache();

        assertThat(repository.findByDirector("Brad Bird"))
                .extracting(AutoconfiguredMovieDocument::getTitle)
                .containsExactlyInAnyOrder("The Iron Giant", "Ratatouille");
    }
}
