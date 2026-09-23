package com.demo.reactive.querymethods.configuration;

import com.demo.reactive.querymethods.entity.ReactiveQueryMethodsMovieDocument;
import com.demo.reactive.querymethods.repository.ReactiveQueryMethodsMovieRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractReactiveAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.repository.config.EnableReactiveAerospikeRepositories;

@Configuration
@EnableReactiveAerospikeRepositories(basePackageClasses = ReactiveQueryMethodsMovieRepository.class)
public class AerospikeConfiguration extends AbstractReactiveAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return ReactiveQueryMethodsMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setScansEnabled(true);
        aerospikeDataSettings.setCreateIndexesOnStartup(true);
    }
}
