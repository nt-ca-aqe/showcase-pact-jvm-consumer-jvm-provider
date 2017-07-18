package com.example.producer;

import static java.util.stream.Collectors.toSet;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Set;
import java.util.UUID;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/movies")
public class MoviesController {

    private final MovieDatabase database;

    public MoviesController(MovieDatabase database) {
        this.database = database;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Movie> post(@RequestBody Movie movie) {
        PersistedMovie persistedMovie = database.create(movie);
        return toMovieResource(persistedMovie);
    }

    @GetMapping
    public Resources<Resource<Movie>> get() {
        Set<Resource<Movie>> movies = database.getAll().stream()//
            .map(this::toMovieResource)//
            .collect(toSet());
        Link selfLink = linkTo(methodOn(MoviesController.class).get()).withSelfRel();
        return new Resources<>(movies, selfLink);
    }

    @GetMapping("/{id}")
    public Resource<Movie> get(@PathVariable UUID id) {
        PersistedMovie persistedMovie = database.getById(id).orElseThrow(NotFoundException::new);
        return toMovieResource(persistedMovie);
    }

    private Resource<Movie> toMovieResource(PersistedMovie persistedMovie) {
        Link selfLink = linkTo(methodOn(MoviesController.class).get(persistedMovie.getId())).withSelfRel();
        return new Resource<>(persistedMovie.getMovie(), selfLink);
    }

}
