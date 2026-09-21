package com.demo.springbootautoconfigure.repository;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface AutoconfiguredMovieRepository extends AerospikeRepository<AutoconfiguredMovieDocument, String> {

}
