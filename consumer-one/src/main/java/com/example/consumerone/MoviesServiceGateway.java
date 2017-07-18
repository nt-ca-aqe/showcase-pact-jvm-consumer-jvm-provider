package com.example.consumerone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class MoviesServiceGateway {

    private final RestTemplate restTemplate;
    private final MoviesServiceConfig config;

    public MoviesServiceGateway(RestTemplate restTemplate, MoviesServiceConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public List<Movie> getMovies() {
        ResponseEntity<MovieListResource> response =
            restTemplate.exchange(config.getUrl() + "/movies", HttpMethod.GET, HttpEntity.EMPTY, MovieListResource.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Resources<Movie> responseBody = response.getBody();
            return new ArrayList<>(responseBody.getContent());
        }
        throw new IllegalStateException("server responded with: " + response);
    }

    public Optional<Movie> getMovie(String id) {
        ResponseEntity<Movie> response =
            restTemplate.exchange(config.getUrl() + "/movies/" + id, HttpMethod.GET, HttpEntity.EMPTY, Movie.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Optional.of(response.getBody());
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
        }
        throw new IllegalStateException("server responded with: " + response);
    }

    private static class MovieListResource extends Resources<Movie> {

    }

}
