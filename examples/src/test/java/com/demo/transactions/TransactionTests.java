package com.demo.transactions;

import com.demo.transactions.entity.TransactionalMovieDocument;
import com.demo.transactions.repository.TransactionalMovieRepository;
import com.demo.transactions.service.TransactionalMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.aerospike.core.AerospikeTemplate;
import org.springframework.data.aerospike.server.version.ServerVersionSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// Transactions require Aerospike Server 8.0+ and a namespace with a properly configured roster
public class TransactionTests extends TransactionsAerospikeDemoApplicationTest {

    @Autowired
    TransactionalMovieRepository repository;

    @Autowired
    TransactionalMovieService service;

    @Autowired
    AerospikeTemplate template;

    @Autowired
    ServerVersionSupport serverVersionSupport;

    @BeforeEach
    void setUp() {
        template.deleteAll(TransactionalMovieDocument.class);
    }

    @Test
    void transactionCommitsRepositoryAndTemplateWrites() {
        assumeTransactionsSupported();
        runOrSkipIfNamespaceDoesNotSupportTransactions(service::saveCommittedMovies);

        assertThat(repository.count()).isEqualTo(2);
    }

    @Test
    void transactionRollsBackDuplicateInsert() {
        assumeTransactionsSupported();
        runOrSkipIfNamespaceDoesNotSupportTransactions(() -> assertThatThrownBy(service::rollbackDuplicateInsert)
            .isInstanceOf(DuplicateKeyException.class));

        assertThat(repository.existsById("transaction-duplicate")).isFalse();
    }

    private void assumeTransactionsSupported() {
        // Transactions require Aerospike Server 8.0+ and a namespace with a properly configured roster
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
