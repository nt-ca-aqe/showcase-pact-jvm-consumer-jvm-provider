package com.example.provider;

import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;


@Service
public class MovieDatabase {

    private final Map<UUID, Movie> movies = new HashMap<>();

    public PersistedMovie create(Movie movie) {
        UUID id = UUID.randomUUID();
        movies.put(id, movie);
        return new PersistedMovie(id, movie);
    }

    public PersistedMovie createOrUpdate(UUID id, Movie movie) {
        movies.put(id, movie);
        return new PersistedMovie(id, movie);
    }

    public Set<PersistedMovie> getAll() {
        return movies.entrySet().stream()//
            .map(entry -> new PersistedMovie(entry.getKey(), entry.getValue()))//
            .collect(toSet());
    }

    public Optional<PersistedMovie> getById(UUID id) {
        return Optional.ofNullable(movies.get(id))//
            .map(movie -> new PersistedMovie(id, movie));
    }

}
