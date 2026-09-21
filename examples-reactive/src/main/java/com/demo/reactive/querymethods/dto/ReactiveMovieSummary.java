package com.demo.reactive.querymethods.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactiveMovieSummary {

    private String title;
    private int releaseYear;
}
