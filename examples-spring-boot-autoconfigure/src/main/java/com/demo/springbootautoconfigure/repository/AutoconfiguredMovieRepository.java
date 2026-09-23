package com.demo.springbootautoconfigure.repository;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import org.springframework.data.aerospike.repository.AerospikeRepository;

import java.util.List;

public interface AutoconfiguredMovieRepository extends AerospikeRepository<AutoconfiguredMovieDocument, String> {

    List<AutoconfiguredMovieDocument> findByDirector(String director);
}
