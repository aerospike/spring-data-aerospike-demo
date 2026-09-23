package com.demo.mappingmetadata.classkey.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Value
@Document(collection = NoClassBinMovieDocument.SET_NAME)
@AllArgsConstructor
public class NoClassBinMovieDocument {

    public static final String SET_NAME = "demo-mappingMetadata-noClassBin-set";

    @Id
    String id;

    @Field
    String title;

    @Field
    int releaseYear;
}
