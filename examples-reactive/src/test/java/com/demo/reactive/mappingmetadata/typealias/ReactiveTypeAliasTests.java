package com.demo.reactive.mappingmetadata.typealias;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.demo.reactive.mappingmetadata.typealias.entity.ReactiveAliasedMovieDocument;
import com.demo.reactive.mappingmetadata.typealias.repository.ReactiveAliasedMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveTypeAliasTests extends ReactiveTypeAliasAerospikeDemoApplicationTest {

    @Autowired
    ReactiveAliasedMovieRepository repository;

    @Autowired
    ReactiveAerospikeTemplate template;

    @Autowired
    IAerospikeClient client;

    @Value("${spring.data.aerospike.namespace}")
    String namespace;

    @BeforeEach
    void setUp() {
        template.deleteAll(ReactiveAliasedMovieDocument.class).block();
    }

    @Test
    void typeAliasStoresStableAliasInsteadOfJavaClassName() {
        ReactiveAliasedMovieDocument movie =
            new ReactiveAliasedMovieDocument("reactive-type-alias-1", "Tampopo", 1985);

        repository.save(movie).block();

        Record rawRecord = client.get(null, new Key(namespace, ReactiveAliasedMovieDocument.SET_NAME, movie.getId()));
        assertThat(rawRecord).isNotNull();
        assertThat(rawRecord.bins).containsEntry("@_class", ReactiveAliasedMovieDocument.TYPE_ALIAS);
        assertThat(rawRecord.bins).doesNotContainEntry("@_class", ReactiveAliasedMovieDocument.class.getName());

        StepVerifier.create(repository.findById(movie.getId()))
            .expectNext(movie)
            .verifyComplete();
    }
}
