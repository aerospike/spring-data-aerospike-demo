package com.demo.springbootautoconfigure.dto;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;

public record MovieRequest(String id, String title, String director, int releaseYear) {

    public AutoconfiguredMovieDocument toDocument() {
        return AutoconfiguredMovieDocument.builder()
                .id(id)
                .title(title)
                .director(director)
                .releaseYear(releaseYear)
                .build();
    }
}
