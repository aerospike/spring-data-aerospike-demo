package com.demo.reactive.mappingmetadata.customconverter;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.demo.reactive.mappingmetadata.customconverter.configuration.AerospikeConfiguration;
import com.demo.reactive.mappingmetadata.customconverter.entity.MovieCode;
import com.demo.reactive.mappingmetadata.customconverter.entity.ReactiveCustomTypeBinMovieDocument;
import com.demo.reactive.mappingmetadata.customconverter.repository.ReactiveCustomTypeBinMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.aerospike.convert.MappingAerospikeConverter;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class ReactiveCustomMappingConverterTests extends ReactiveCustomMappingConverterAerospikeDemoApplicationTest {

    @Autowired
    ReactiveCustomTypeBinMovieRepository repository;

    @Autowired
    ReactiveAerospikeTemplate template;

    @Autowired
    IAerospikeClient client;

    @Autowired
    MappingAerospikeConverter converter;

    @Value("${spring.data.aerospike.namespace}")
    String namespace;

    @BeforeEach
    void setUp() {
        template.deleteAll(ReactiveCustomTypeBinMovieDocument.class).block();
        template.refreshIndexesCache().block();
    }

    @Test
    void customTypeMetadataBinAndConvertersKeepStorageReadsAndQueriesConsistent() {
        MovieCode code = new MovieCode("RGHB-1991");
        ReactiveCustomTypeBinMovieDocument movie =
            new ReactiveCustomTypeBinMovieDocument("reactive-custom-converter-1", "Only Yesterday", 1991, code);

        repository.save(movie).block();

        Record rawRecord = client.get(
            null, new Key(namespace, ReactiveCustomTypeBinMovieDocument.SET_NAME, movie.getId()));
        assertThat(rawRecord).isNotNull();
        assertThat(rawRecord.bins)
            .containsEntry(AerospikeConfiguration.TYPE_METADATA_BIN, ReactiveCustomTypeBinMovieDocument.TYPE_ALIAS)
            .containsEntry("code", code.value())
            .doesNotContainKey("@_class");
        assertThat(converter.getAerospikeDataSettings().getClassKey())
            .isEqualTo(AerospikeConfiguration.TYPE_METADATA_BIN);

        StepVerifier.create(repository.findById(movie.getId()))
            .expectNext(movie)
            .verifyComplete();
        StepVerifier.create(repository.findByCode(code).collectList())
            .assertNext(results -> assertThat(results).containsExactly(movie))
            .verifyComplete();
    }
}
