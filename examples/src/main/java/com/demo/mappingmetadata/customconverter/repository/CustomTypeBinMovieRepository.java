package com.demo.mappingmetadata.customconverter.repository;

import com.demo.mappingmetadata.customconverter.entity.CustomTypeBinMovieDocument;
import com.demo.mappingmetadata.customconverter.entity.MovieCode;
import org.springframework.data.aerospike.repository.AerospikeRepository;

import java.util.List;

public interface CustomTypeBinMovieRepository extends AerospikeRepository<CustomTypeBinMovieDocument, String> {

    List<CustomTypeBinMovieDocument> findByCode(MovieCode code);
}
