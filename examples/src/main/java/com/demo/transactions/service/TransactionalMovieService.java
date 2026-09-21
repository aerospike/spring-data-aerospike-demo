package com.demo.transactions.service;

import com.demo.transactions.entity.TransactionalMovieDocument;
import com.demo.transactions.repository.TransactionalMovieRepository;
import org.springframework.data.aerospike.core.AerospikeTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalMovieService {

    private final TransactionalMovieRepository repository;
    private final AerospikeTemplate template;

    public TransactionalMovieService(TransactionalMovieRepository repository, AerospikeTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    @Transactional(transactionManager = "aerospikeTransactionManager")
    public void saveCommittedMovies() {
        repository.save(new TransactionalMovieDocument("transaction-1", "The Conversation", "committed"));
        template.save(new TransactionalMovieDocument("transaction-2", "Michael Clayton", "committed"));
    }

    @Transactional(transactionManager = "aerospikeTransactionManager")
    public void rollbackDuplicateInsert() {
        TransactionalMovieDocument duplicate =
            new TransactionalMovieDocument("transaction-duplicate", "Duplicate", "rollback");

        template.insert(duplicate);
        template.insert(duplicate);
    }
}
