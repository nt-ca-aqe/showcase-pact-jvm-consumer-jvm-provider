package com.example.provider.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.provider.core.Movie;
import com.example.provider.core.MovieDatabase;
import com.example.provider.core.MovieRecord;

@RestController
@RequestMapping("/movies")
class MoviesController {

    private final MovieDatabase database;

    public MoviesController(MovieDatabase database) {
        this.database = database;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Resource<Movie> post(@RequestBody Movie movie) {
        MovieRecord record = database.add(movie);
        return toMovieResource(record);
    }

    @GetMapping
    MoviesResource getAll(@RequestParam(required=false) Float score) {
        Set<Resource<Movie>> movies = database.findAll(score)//
        		.map(this::toMovieResource)
        		.collect(Collectors.toSet());

        MoviesResource result = new MoviesResource(movies);
        Link selfLink = linkTo(methodOn(MoviesController.class).getAll(score)).withSelfRel();
        result.add(selfLink);

		return result;
    }

    @GetMapping("/{id}")
    Resource<Movie> get(@PathVariable UUID id) {
        MovieRecord record = database.findById(id);
        return toMovieResource(record);
    }

    private Resource<Movie> toMovieResource(MovieRecord movieRecord) {
        Link selfLink = linkTo(methodOn(MoviesController.class).get(movieRecord.getId())).withSelfRel();
        return new Resource<>(movieRecord.getMovie(), selfLink);
    }

}
