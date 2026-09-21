package com.demo.mappingmetadata.typealias;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.demo.mappingmetadata.typealias.entity.AliasedMovieDocument;
import com.demo.mappingmetadata.typealias.repository.AliasedMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeAliasTests extends TypeAliasAerospikeDemoApplicationTest {

    @Autowired
    AliasedMovieRepository repository;

    @Autowired
    AerospikeTemplate template;

    @Autowired
    IAerospikeClient client;

    @Value("${spring.data.aerospike.namespace}")
    String namespace;

    @BeforeEach
    void setUp() {
        template.deleteAll(AliasedMovieDocument.class);
    }

    @Test
    void typeAliasStoresStableAliasInsteadOfJavaClassName() {
        AliasedMovieDocument movie = new AliasedMovieDocument("type-alias-1", "Tampopo", 1985);

        repository.save(movie);

        Record rawRecord = client.get(null, new Key(namespace, AliasedMovieDocument.SET_NAME, movie.getId()));
        assertThat(rawRecord).isNotNull();
        assertThat(rawRecord.bins).containsEntry("@_class", AliasedMovieDocument.TYPE_ALIAS);
        assertThat(rawRecord.bins).doesNotContainEntry("@_class", AliasedMovieDocument.class.getName());
        assertThat(repository.findById(movie.getId())).hasValue(movie);
    }
}
