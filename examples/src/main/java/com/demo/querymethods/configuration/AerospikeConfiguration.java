package com.demo.querymethods.configuration;

import com.demo.querymethods.entity.QueryMethodsMovieDocument;
import com.demo.querymethods.repository.QueryMethodsMovieRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

@Configuration
@EnableAerospikeRepositories(basePackageClasses = QueryMethodsMovieRepository.class)
public class AerospikeConfiguration extends AbstractAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return QueryMethodsMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setScansEnabled(true);
        aerospikeDataSettings.setCreateIndexesOnStartup(true);
    }
}
