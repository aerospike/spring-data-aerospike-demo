package com.demo.springbootautoconfigure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Value
@Document(collection = "demo-springboot-autoconfigure-set")
@Builder(toBuilder = true)
@AllArgsConstructor
public class AutoconfiguredMovieDocument {

    @Id
    String id;

    @Field
    String title;

    @Field
    String director;

    @Field
    int releaseYear;
}
