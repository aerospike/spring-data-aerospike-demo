package com.demo.mappingmetadata.classkey;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.demo.mappingmetadata.classkey.entity.NoClassBinMovieDocument;
import com.demo.mappingmetadata.classkey.repository.NoClassBinMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassKeyDisabledTests extends ClassKeyDisabledAerospikeDemoApplicationTest {

    @Autowired
    NoClassBinMovieRepository repository;

    @Autowired
    AerospikeTemplate template;

    @Autowired
    IAerospikeClient client;

    @Value("${spring.data.aerospike.namespace}")
    String namespace;

    @BeforeEach
    void setUp() {
        template.deleteAll(NoClassBinMovieDocument.class);
    }

    @Test
    void emptyClassKeySkipsClassMetadataBinButStillReadsDeclaredType() {
        NoClassBinMovieDocument movie =
            new NoClassBinMovieDocument("no-class-bin-1", "The Straight Story", 1999);

        repository.save(movie);

        Record rawRecord = client.get(null, new Key(namespace, NoClassBinMovieDocument.SET_NAME, movie.getId()));
        assertThat(rawRecord).isNotNull();
        assertThat(rawRecord.bins).doesNotContainKey("@_class");
        assertThat(repository.findById(movie.getId())).hasValue(movie);
    }
}
