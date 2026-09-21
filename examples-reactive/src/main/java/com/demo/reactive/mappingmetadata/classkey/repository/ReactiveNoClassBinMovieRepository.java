package com.demo.reactive.mappingmetadata.classkey.repository;

import com.demo.reactive.mappingmetadata.classkey.entity.ReactiveNoClassBinMovieDocument;
import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository;

public interface ReactiveNoClassBinMovieRepository
    extends ReactiveAerospikeRepository<ReactiveNoClassBinMovieDocument, String> {
}
