package com.demo.reactive.mappingmetadata.classkey;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.demo.reactive.mappingmetadata.classkey.entity.ReactiveNoClassBinMovieDocument;
import com.demo.reactive.mappingmetadata.classkey.repository.ReactiveNoClassBinMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveClassKeyDisabledTests extends ReactiveClassKeyDisabledAerospikeDemoApplicationTest {

    @Autowired
    ReactiveNoClassBinMovieRepository repository;

    @Autowired
    ReactiveAerospikeTemplate template;

    @Autowired
    IAerospikeClient client;

    @Value("${spring.data.aerospike.namespace}")
    String namespace;

    @BeforeEach
    void setUp() {
        template.deleteAll(ReactiveNoClassBinMovieDocument.class).block();
    }

    @Test
    void emptyClassKeySkipsClassMetadataBinButStillReadsDeclaredType() {
        ReactiveNoClassBinMovieDocument movie =
            new ReactiveNoClassBinMovieDocument("reactive-no-class-bin-1", "The Straight Story", 1999);

        repository.save(movie).block();

        Record rawRecord = client.get(null, new Key(namespace, ReactiveNoClassBinMovieDocument.SET_NAME, movie.getId()));
        assertThat(rawRecord).isNotNull();
        assertThat(rawRecord.bins).doesNotContainKey("@_class");

        StepVerifier.create(repository.findById(movie.getId()))
            .expectNext(movie)
            .verifyComplete();
    }
}
