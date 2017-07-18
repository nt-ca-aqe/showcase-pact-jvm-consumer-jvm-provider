package com.example.provider;

import java.util.UUID;


public class PersistedMovie {

    private final UUID id;
    private final Movie movie;

    public PersistedMovie(UUID id, Movie movie) {
        this.id = id;
        this.movie = movie;
    }

    public UUID getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

}
