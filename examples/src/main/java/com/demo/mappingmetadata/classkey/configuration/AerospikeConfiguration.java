package com.demo.mappingmetadata.classkey.configuration;

import com.demo.mappingmetadata.classkey.entity.NoClassBinMovieDocument;
import com.demo.mappingmetadata.classkey.repository.NoClassBinMovieRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;

@Configuration
@EnableAerospikeRepositories(basePackageClasses = NoClassBinMovieRepository.class)
public class AerospikeConfiguration extends AbstractAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return NoClassBinMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        /*
         * Spring Data Aerospike writes @_class by default. Setting an empty classKey suppresses that metadata bin for
         * interoperability with readers, writers, buses, or serializers that do not expect a type alias bin. Use this
         * only when declared-type reads are enough; polymorphic reads need type metadata.
         */
        aerospikeDataSettings.setClassKey("");
        aerospikeDataSettings.setScansEnabled(true);
    }
}
