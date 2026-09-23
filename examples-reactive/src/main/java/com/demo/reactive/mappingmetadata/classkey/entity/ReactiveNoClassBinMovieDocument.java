package com.demo.reactive.mappingmetadata.classkey.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Value
@Document(collection = ReactiveNoClassBinMovieDocument.SET_NAME)
@AllArgsConstructor
public class ReactiveNoClassBinMovieDocument {

    public static final String SET_NAME = "demo-mappingMetadata-reactive-noClassBin-set";

    @Id
    String id;

    @Field
    String title;

    @Field
    int releaseYear;
}
