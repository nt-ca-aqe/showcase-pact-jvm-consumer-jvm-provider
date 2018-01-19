package com.example.provider.core;

import java.util.UUID;

import com.example.provider.core.Movie;


public class MovieRecord {

    private final UUID id;
    private final Movie movie;

    public MovieRecord(UUID id, Movie movie) {
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
