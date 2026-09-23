package com.demo.springbootautoconfigure.entity;

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
@Document(collection = "demo-springboot-autoconfigure-set")
@Builder(toBuilder = true)
@AllArgsConstructor
public class AutoconfiguredMovieDocument {

    public static final String DIRECTOR_INDEX = "demo-springboot-autoconfigure-director-index";

    @Id
    String id;

    @Field
    String title;

    @Indexed(name = DIRECTOR_INDEX, type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    @Field
    String director;

    @Field
    int releaseYear;
}
