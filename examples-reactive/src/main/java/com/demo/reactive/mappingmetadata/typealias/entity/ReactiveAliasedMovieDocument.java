package com.demo.reactive.mappingmetadata.typealias.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

@Value
@TypeAlias(ReactiveAliasedMovieDocument.TYPE_ALIAS)
@Document(collection = ReactiveAliasedMovieDocument.SET_NAME)
@AllArgsConstructor
public class ReactiveAliasedMovieDocument {

    public static final String SET_NAME = "demo-mappingMetadata-reactive-typeAlias-set";
    public static final String TYPE_ALIAS = "reactive-mapping-metadata-movie";

    @Id
    String id;

    @Field
    String title;

    @Field
    int releaseYear;
}
