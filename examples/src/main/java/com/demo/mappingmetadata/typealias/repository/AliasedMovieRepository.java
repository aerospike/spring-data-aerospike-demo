package com.demo.mappingmetadata.typealias.repository;

import com.demo.mappingmetadata.typealias.entity.AliasedMovieDocument;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface AliasedMovieRepository extends AerospikeRepository<AliasedMovieDocument, String> {
}
