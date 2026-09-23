package com.demo.mappingmetadata.customconverter;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.demo.mappingmetadata.customconverter.configuration.AerospikeConfiguration;
import com.demo.mappingmetadata.customconverter.entity.CustomTypeBinMovieDocument;
import com.demo.mappingmetadata.customconverter.entity.MovieCode;
import com.demo.mappingmetadata.customconverter.repository.CustomTypeBinMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.aerospike.convert.MappingAerospikeConverter;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomMappingConverterTests extends CustomMappingConverterAerospikeDemoApplicationTest {

    @Autowired
    CustomTypeBinMovieRepository repository;

    @Autowired
    AerospikeTemplate template;

    @Autowired
    IAerospikeClient client;

    @Autowired
    MappingAerospikeConverter converter;

    @Value("${spring.data.aerospike.namespace}")
    String namespace;

    @BeforeEach
    void setUp() {
        template.deleteAll(CustomTypeBinMovieDocument.class);
        template.refreshIndexesCache();
    }

    @Test
    void customTypeMetadataBinAndConvertersKeepStorageReadsAndQueriesConsistent() {
        MovieCode code = new MovieCode("GHB-1991");
        CustomTypeBinMovieDocument movie =
            new CustomTypeBinMovieDocument("custom-converter-1", "Only Yesterday", 1991, code);

        repository.save(movie);

        Record rawRecord = client.get(null, new Key(namespace, CustomTypeBinMovieDocument.SET_NAME, movie.getId()));
        assertThat(rawRecord).isNotNull();
        assertThat(rawRecord.bins)
            .containsEntry(AerospikeConfiguration.TYPE_METADATA_BIN, CustomTypeBinMovieDocument.TYPE_ALIAS)
            .containsEntry("code", code.value())
            .doesNotContainKey("@_class");
        assertThat(converter.getAerospikeDataSettings().getClassKey())
            .isEqualTo(AerospikeConfiguration.TYPE_METADATA_BIN);
        assertThat(repository.findById(movie.getId())).hasValue(movie);
        assertThat(repository.findByCode(code)).containsExactly(movie);
    }
}
