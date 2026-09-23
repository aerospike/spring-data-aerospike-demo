package com.demo.springbootautoconfigure.service;

import com.demo.springbootautoconfigure.entity.AutoconfiguredMovieDocument;
import com.demo.springbootautoconfigure.repository.AutoconfiguredMovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieCatalogService {

    private final AutoconfiguredMovieRepository repository;

    public MovieCatalogService(AutoconfiguredMovieRepository repository) {
        this.repository = repository;
    }

    public AutoconfiguredMovieDocument saveMovie(AutoconfiguredMovieDocument movie) {
        return repository.save(movie);
    }

    public Optional<AutoconfiguredMovieDocument> findMovieById(String id) {
        return repository.findById(id);
    }

    public List<AutoconfiguredMovieDocument> findMoviesByDirector(String director) {
        return repository.findByDirector(director);
    }

    public void deleteMovie(String id) {
        repository.deleteById(id);
    }
}
