package com.demo.reactive.mappingmetadata.customconverter.repository;

import com.demo.reactive.mappingmetadata.customconverter.entity.MovieCode;
import com.demo.reactive.mappingmetadata.customconverter.entity.ReactiveCustomTypeBinMovieDocument;
import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository;
import reactor.core.publisher.Flux;

public interface ReactiveCustomTypeBinMovieRepository
    extends ReactiveAerospikeRepository<ReactiveCustomTypeBinMovieDocument, String> {

    Flux<ReactiveCustomTypeBinMovieDocument> findByCode(MovieCode code);
}
