package com.demo.reactive.templateops.entity;

import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.aerospike.annotation.Indexed;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Data
@Document(collection = "demo-templateOps-reactive-set")
@NoArgsConstructor
@AllArgsConstructor
public class ReactiveTemplateMovieDocument {

    public static final String GENRE_INDEX = "demo-templateOps-reactive-genre-index";
    public static final String RELEASE_YEAR_INDEX = "demo-templateOps-reactive-releaseYear-index";

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

    @Field
    int rating;

    @Field
    long views;
}
