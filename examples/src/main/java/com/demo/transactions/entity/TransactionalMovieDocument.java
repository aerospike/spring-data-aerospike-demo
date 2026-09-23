package com.demo.transactions.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Value
@Document(collection = "demo-transactions-set")
@AllArgsConstructor
public class TransactionalMovieDocument {

    @Id
    String id;

    @Field
    String title;

    @Field
    String status;
}
