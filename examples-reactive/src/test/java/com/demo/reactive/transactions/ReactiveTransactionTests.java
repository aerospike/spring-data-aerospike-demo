package com.demo.reactive.transactions;

import com.demo.reactive.transactions.entity.ReactiveTransactionalMovieDocument;
import com.demo.reactive.transactions.repository.ReactiveTransactionalMovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;
import org.springframework.data.aerospike.server.version.ServerVersionSupport;
import org.springframework.transaction.reactive.TransactionalOperator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// Transactions require Aerospike Server 8.0+ and a namespace with a properly configured roster.
public class ReactiveTransactionTests extends ReactiveTransactionsAerospikeDemoApplicationTest {

    @Autowired
    ReactiveTransactionalMovieRepository repository;

    @Autowired
    ReactiveAerospikeTemplate template;

    @Autowired
    TransactionalOperator transactionalOperator;

    @Autowired
    ServerVersionSupport serverVersionSupport;

    @BeforeEach
    void setUp() {
        template.deleteAll(ReactiveTransactionalMovieDocument.class).block();
    }

    @Test
    void transactionCommitsRepositoryAndTemplateWrites() {
        assumeTransactionsSupported();
        runOrSkipIfNamespaceDoesNotSupportTransactions(() -> repository
            .save(new ReactiveTransactionalMovieDocument("reactive-transaction-1", "The Conversation", "committed"))
            .then(template.save(new ReactiveTransactionalMovieDocument(
                "reactive-transaction-2", "Michael Clayton", "committed")))
            .then()
            .as(transactionalOperator::transactional)
            .block());

        org.assertj.core.api.Assertions.assertThat(repository.count().block()).isEqualTo(2L);
    }

    @Test
    void transactionRollsBackDuplicateInsert() {
        assumeTransactionsSupported();
        ReactiveTransactionalMovieDocument duplicate =
            new ReactiveTransactionalMovieDocument("reactive-transaction-duplicate", "Duplicate", "rollback");

        runOrSkipIfNamespaceDoesNotSupportTransactions(() -> assertThatThrownBy(() -> template.insert(duplicate)
            .then(template.insert(duplicate))
            .then()
            .as(transactionalOperator::transactional)
            .block()).isInstanceOf(DuplicateKeyException.class));

        org.assertj.core.api.Assertions.assertThat(repository.existsById("reactive-transaction-duplicate").block())
            .isFalse();
    }

    private void assumeTransactionsSupported() {
        assumeTrue(serverVersionSupport.isTxnSupported(), "Aerospike transactions require Server 8.0.0+");
    }

    private void runOrSkipIfNamespaceDoesNotSupportTransactions(Executable executable) {
        try {
            executable.execute();
        } catch (Throwable failure) {
            assumeFalse(isUnsupportedTransactionFeature(failure),
                "Aerospike transactions require a transaction-enabled namespace");
            throw new RuntimeException(failure);
        }
    }

    private boolean isUnsupportedTransactionFeature(Throwable failure) {
        Throwable current = failure;
        while (current != null) {
            String message = current.getMessage();
            if (message != null && (message.contains("Unsupported Server Feature")
                || message.contains("Failed to add key(s) to transaction monitor")
                || message.contains("Enterprise only"))) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
