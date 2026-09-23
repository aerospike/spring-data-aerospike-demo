package com.demo.reactive.mappingmetadata.customconverter.configuration;

import com.demo.reactive.mappingmetadata.customconverter.converter.MovieCodeConverters;
import com.demo.reactive.mappingmetadata.customconverter.entity.ReactiveCustomTypeBinMovieDocument;
import com.demo.reactive.mappingmetadata.customconverter.repository.ReactiveCustomTypeBinMovieRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractReactiveAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.convert.AerospikeCustomConverters;
import org.springframework.data.aerospike.repository.config.EnableReactiveAerospikeRepositories;

import java.util.List;

@Configuration
@EnableReactiveAerospikeRepositories(basePackageClasses = ReactiveCustomTypeBinMovieRepository.class)
public class AerospikeConfiguration extends AbstractReactiveAerospikeDataConfiguration {

    public static final String TYPE_METADATA_BIN = "rx_demo_type";

    @Override
    protected String getMappingBasePackage() {
        return ReactiveCustomTypeBinMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setClassKey(TYPE_METADATA_BIN);
        aerospikeDataSettings.setScansEnabled(true);
    }

    @Bean
    public AerospikeCustomConverters movieCodeConverters() {
        return new AerospikeCustomConverters(List.of(
            MovieCodeConverters.MovieCodeToStringConverter.INSTANCE,
            MovieCodeConverters.StringToMovieCodeConverter.INSTANCE
        ));
    }
}
