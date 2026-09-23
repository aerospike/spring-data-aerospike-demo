package com.demo.reactive.mappingmetadata.typealias.repository;

import com.demo.reactive.mappingmetadata.typealias.entity.ReactiveAliasedMovieDocument;
import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository;

public interface ReactiveAliasedMovieRepository
    extends ReactiveAerospikeRepository<ReactiveAliasedMovieDocument, String> {
}
