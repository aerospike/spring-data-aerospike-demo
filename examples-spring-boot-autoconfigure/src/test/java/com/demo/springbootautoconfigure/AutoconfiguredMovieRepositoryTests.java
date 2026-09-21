package com.demo.springbootautoconfigure;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import com.demo.springbootautoconfigure.repository.AutoconfiguredMovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoconfiguredMovieRepositoryTests extends SpringBootAutoconfigureAerospikeDemoApplicationTest {

    String id;
    AutoconfiguredMovieDocument movie;

    @Autowired
    AutoconfiguredMovieRepository repository;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID().toString();
        movie = AutoconfiguredMovieDocument.builder()
                .id(id)
                .title("The Iron Giant")
                .director("Brad Bird")
                .releaseYear(1999)
                .build();
    }

    @AfterEach
    void tearDown() {
        repository.deleteById(id);
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
}
