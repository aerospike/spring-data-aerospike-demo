package com.demo.mappingmetadata.typealias.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

@Value
@TypeAlias(AliasedMovieDocument.TYPE_ALIAS)
@Document(collection = AliasedMovieDocument.SET_NAME)
@AllArgsConstructor
public class AliasedMovieDocument {

    public static final String SET_NAME = "demo-mappingMetadata-typeAlias-set";
    public static final String TYPE_ALIAS = "mapping-metadata-movie";

    @Id
    String id;

    @Field
    String title;

    @Field
    int releaseYear;
}
