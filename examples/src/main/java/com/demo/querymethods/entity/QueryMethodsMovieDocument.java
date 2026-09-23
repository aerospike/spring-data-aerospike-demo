package com.demo.querymethods.entity;

import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.aerospike.annotation.Indexed;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Value
@Document(collection = "demo-queryMethods-set")
@Builder(toBuilder = true)
@AllArgsConstructor
public class QueryMethodsMovieDocument {

    public static final String GENRE_INDEX = "demo-queryMethods-genre-index";
    public static final String RELEASE_YEAR_INDEX = "demo-queryMethods-releaseYear-index";

    @Id
    String id;

    @Field
    String title;

    @Indexed(name = GENRE_INDEX, type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    @Field
    String genre;

    @Indexed(name = RELEASE_YEAR_INDEX, type = IndexType.NUMERIC, collectionType = IndexCollectionType.DEFAULT)
    @Field
    int releaseYear;
}
