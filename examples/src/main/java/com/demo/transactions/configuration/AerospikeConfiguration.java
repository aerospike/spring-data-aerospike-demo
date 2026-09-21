package com.demo.transactions.configuration;

import com.aerospike.client.IAerospikeClient;
import com.demo.transactions.entity.TransactionalMovieDocument;
import com.demo.transactions.repository.TransactionalMovieRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;
import org.springframework.data.aerospike.transaction.sync.AerospikeTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAerospikeRepositories(basePackageClasses = TransactionalMovieRepository.class)
@EnableTransactionManagement
public class AerospikeConfiguration extends AbstractAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return TransactionalMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setScansEnabled(true);
        aerospikeDataSettings.setCreateIndexesOnStartup(false);
    }

    @Bean
    public AerospikeTransactionManager aerospikeTransactionManager(IAerospikeClient client) {
        return new AerospikeTransactionManager(client);
    }
}
