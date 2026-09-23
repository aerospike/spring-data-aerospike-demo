package com.demo.reactive.transactions.repository;

import com.demo.reactive.transactions.entity.ReactiveTransactionalMovieDocument;
import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository;

public interface ReactiveTransactionalMovieRepository
    extends ReactiveAerospikeRepository<ReactiveTransactionalMovieDocument, String> {
}
