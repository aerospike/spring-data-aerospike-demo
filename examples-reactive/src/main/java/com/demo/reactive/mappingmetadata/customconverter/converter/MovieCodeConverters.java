package com.demo.reactive.mappingmetadata.customconverter.converter;

import com.demo.reactive.mappingmetadata.customconverter.entity.MovieCode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

public class MovieCodeConverters {

    @WritingConverter
    public enum MovieCodeToStringConverter implements Converter<MovieCode, String> {
        INSTANCE;

        @Override
        public String convert(MovieCode source) {
            return source.value();
        }
    }

    @ReadingConverter
    public enum StringToMovieCodeConverter implements Converter<String, MovieCode> {
        INSTANCE;

        @Override
        public MovieCode convert(String source) {
            return new MovieCode(source);
        }
    }
}
