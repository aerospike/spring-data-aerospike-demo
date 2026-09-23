package com.demo.springbootautoconfigure.controller;

import com.demo.springbootautoconfigure.dto.MovieRequest;
import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import com.demo.springbootautoconfigure.service.MovieCatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo/movies")
public class MovieCatalogController {

    private final MovieCatalogService movieCatalogService;

    public MovieCatalogController(MovieCatalogService movieCatalogService) {
        this.movieCatalogService = movieCatalogService;
    }

    @PostMapping
    public ResponseEntity<AutoconfiguredMovieDocument> saveMovie(@RequestBody MovieRequest movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieCatalogService.saveMovie(movie.toDocument()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoconfiguredMovieDocument> readMovieById(@PathVariable("id") String id) {
        return movieCatalogService.findMovieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<AutoconfiguredMovieDocument> readMoviesByDirector(@RequestParam("director") String director) {
        return movieCatalogService.findMoviesByDirector(director);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovieById(@PathVariable("id") String id) {
        movieCatalogService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
