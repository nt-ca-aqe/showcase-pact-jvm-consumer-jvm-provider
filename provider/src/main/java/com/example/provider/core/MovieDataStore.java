package com.example.provider.core;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface MovieDataStore {

    MovieRecord create(Movie movie);

    MovieRecord createOrUpdate(UUID id, Movie movie);

    Set<MovieRecord> getAll();

    Optional<MovieRecord> getById(UUID id);

}
