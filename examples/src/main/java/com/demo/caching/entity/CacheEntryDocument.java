package com.demo.caching.entity;

import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = CacheEntryDocument.SET_NAME)
public class CacheEntryDocument {

    public static final String SET_NAME = "demo-cache-entries";

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
