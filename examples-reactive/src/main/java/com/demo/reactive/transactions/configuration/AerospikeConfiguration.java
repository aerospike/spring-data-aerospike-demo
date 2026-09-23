package com.demo.reactive.transactions.configuration;

import com.aerospike.client.reactor.IAerospikeReactorClient;
import com.demo.reactive.transactions.entity.ReactiveTransactionalMovieDocument;
import com.demo.reactive.transactions.repository.ReactiveTransactionalMovieRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractReactiveAerospikeDataConfiguration;
import org.springframework.data.aerospike.config.AerospikeDataSettings;
import org.springframework.data.aerospike.repository.config.EnableReactiveAerospikeRepositories;
import org.springframework.data.aerospike.transaction.reactive.AerospikeReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Configuration
@EnableReactiveAerospikeRepositories(basePackageClasses = ReactiveTransactionalMovieRepository.class)
@EnableTransactionManagement
public class AerospikeConfiguration extends AbstractReactiveAerospikeDataConfiguration {

    @Override
    protected String getMappingBasePackage() {
        return ReactiveTransactionalMovieDocument.class.getPackageName();
    }

    @Override
    protected void configureDataSettings(AerospikeDataSettings aerospikeDataSettings) {
        aerospikeDataSettings.setScansEnabled(true);
        aerospikeDataSettings.setCreateIndexesOnStartup(false);
    }

    @Bean
    public AerospikeReactiveTransactionManager aerospikeReactiveTransactionManager(IAerospikeReactorClient client) {
        return new AerospikeReactiveTransactionManager(client);
    }

    @Bean
    public TransactionalOperator transactionalOperator(AerospikeReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager, new DefaultTransactionDefinition());
    }
}
