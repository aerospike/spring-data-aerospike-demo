package com.demo.reactive.mappingmetadata.classkey.configuration;

import com.demo.reactive.mappingmetadata.classkey.entity.ReactiveNoClassBinMovieDocument;
import com.demo.reactive.mappingmetadata.classkey.repository.ReactiveNoClassBinMovieRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractReactiveAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.repository.config.EnableReactiveAerospikeRepositories;

@Configuration
@EnableReactiveAerospikeRepositories(basePackageClasses = ReactiveNoClassBinMovieRepository.class)
public class AerospikeConfiguration extends AbstractReactiveAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return ReactiveNoClassBinMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setClassKey("");
        aerospikeDataSettings.setScansEnabled(true);
    }
}
