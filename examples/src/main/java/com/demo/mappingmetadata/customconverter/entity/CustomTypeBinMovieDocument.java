package com.demo.mappingmetadata.customconverter.entity;

import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.aerospike.annotation.Indexed;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;

@Value
@TypeAlias(CustomTypeBinMovieDocument.TYPE_ALIAS)
@Document(collection = CustomTypeBinMovieDocument.SET_NAME)
@AllArgsConstructor
public class CustomTypeBinMovieDocument {

    public static final String SET_NAME = "demo-mappingMetadata-customConverter-set";
    public static final String TYPE_ALIAS = "custom-converter-movie";
    public static final String CODE_INDEX = "demo-mappingMetadata-customConverter-code-index";

    @Id
    String id;

    @Field
    String title;

    @Field
    int releaseYear;

    @Indexed(name = CODE_INDEX, type = IndexType.STRING, collectionType = IndexCollectionType.DEFAULT)
    @Field
    MovieCode code;
}
