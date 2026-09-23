package com.demo.autoconfigure.aerospike;

import com.aerospike.client.IAerospikeClient;
import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import com.demo.springbootautoconfigure.repository.AutoconfiguredMovieRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.repository.AerospikeRepository;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

/*
 * Demo-only auto-configuration that plays the role of a reusable starter.
 * Application code consumes this through AutoConfiguration.imports and regular
 * spring.aerospike / spring.data.aerospike properties.
 */
@AutoConfiguration
@ConditionalOnClass({IAerospikeClient.class, AerospikeRepository.class})
@EnableAerospikeRepositories(basePackageClasses = AutoconfiguredMovieRepository.class)
public class SpringDataAerospikeDemoAutoConfiguration extends AbstractAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return AutoconfiguredMovieDocument.class.getPackageName();
    }
}
