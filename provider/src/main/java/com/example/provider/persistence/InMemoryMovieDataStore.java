package com.example.provider.persistence;

import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.provider.core.Movie;
import com.example.provider.core.MovieDataStore;
import com.example.provider.core.MovieRecord;


@Service
class InMemoryMovieDataStore implements MovieDataStore {

    private final Map<UUID, Movie> movies = new HashMap<>();

    @Override
    public MovieRecord create(Movie movie) {
        UUID id = UUID.randomUUID();
        movies.put(id, movie);
        return new MovieRecord(id, movie);
    }

    @Override
    public MovieRecord createOrUpdate(UUID id, Movie movie) {
        movies.put(id, movie);
        return new MovieRecord(id, movie);
    }

    @Override
    public Set<MovieRecord> getAll() {
        return movies.entrySet().stream()//
            .map(entry -> new MovieRecord(entry.getKey(), entry.getValue()))//
            .collect(toSet());
    }

    @Override
    public Optional<MovieRecord> getById(UUID id) {
        return Optional.ofNullable(movies.get(id))//
            .map(movie -> new MovieRecord(id, movie));
    }

}
