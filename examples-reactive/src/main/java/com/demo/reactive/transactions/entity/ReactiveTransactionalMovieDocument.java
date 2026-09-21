package com.demo.reactive.transactions.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Value
@Document(collection = "demo-transactions-reactive-set")
@AllArgsConstructor
public class ReactiveTransactionalMovieDocument {

    @Id
    String id;

    @Field
    String title;

    @Field
    String status;
}
