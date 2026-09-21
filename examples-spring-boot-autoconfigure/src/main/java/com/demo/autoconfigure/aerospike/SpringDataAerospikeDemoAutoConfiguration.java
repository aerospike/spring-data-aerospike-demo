package com.demo.autoconfigure.aerospike;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import com.demo.springbootautoconfigure.repository.AutoconfiguredMovieRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

@AutoConfiguration
@EnableAerospikeRepositories(basePackageClasses = AutoconfiguredMovieRepository.class)
public class SpringDataAerospikeDemoAutoConfiguration extends AbstractAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return AutoconfiguredMovieDocument.class.getPackageName();
    }
}
