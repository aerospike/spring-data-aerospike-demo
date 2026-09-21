package com.demo.transactions.repository;

import com.demo.transactions.entity.TransactionalMovieDocument;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface TransactionalMovieRepository extends AerospikeRepository<TransactionalMovieDocument, String> {
}
