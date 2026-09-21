package com.demo.mappingmetadata.customconverter.configuration;

import com.demo.mappingmetadata.customconverter.converter.MovieCodeConverters;
import com.demo.mappingmetadata.customconverter.entity.CustomTypeBinMovieDocument;
import com.demo.mappingmetadata.customconverter.repository.CustomTypeBinMovieRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.convert.AerospikeCustomConverters;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

import java.util.List;

@Configuration
@EnableAerospikeRepositories(basePackageClasses = CustomTypeBinMovieRepository.class)
public class AerospikeConfiguration extends AbstractAerospikeDataConfiguration {

    public static final String TYPE_METADATA_BIN = "demo_type";

    @Override
    protected String getMappingBasePackage() {
        return CustomTypeBinMovieDocument.class.getPackageName();
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
