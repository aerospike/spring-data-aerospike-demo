package com.demo.mappingmetadata.classkey.repository;

import com.demo.mappingmetadata.classkey.entity.NoClassBinMovieDocument;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface NoClassBinMovieRepository extends AerospikeRepository<NoClassBinMovieDocument, String> {
}
