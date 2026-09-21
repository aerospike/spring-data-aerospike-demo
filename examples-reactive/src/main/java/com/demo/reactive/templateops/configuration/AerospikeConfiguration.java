package com.demo.reactive.templateops.configuration;

import com.demo.reactive.templateops.entity.ReactiveTemplateMovieDocument;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractReactiveAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;

@Configuration
public class AerospikeConfiguration extends AbstractReactiveAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return ReactiveTemplateMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setScansEnabled(false);
        aerospikeDataSettings.setCreateIndexesOnStartup(true);
    }
}
