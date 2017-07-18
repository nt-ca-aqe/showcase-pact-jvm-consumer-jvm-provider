package com.example.producer;

import static java.util.stream.Collectors.toSet;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@ComponentScan
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Component
    public static class DataSetup implements CommandLineRunner {

        private final MovieDatabase database;

        public DataSetup(MovieDatabase database) {
            this.database = database;
        }

        @Override
        public void run(String... args) throws Exception {

            Movie batmanBegins = new Movie();
            batmanBegins.title = "Batman Begins";
            batmanBegins.description = "Lorem Ipsum ...";
            batmanBegins.releaseYear = 2005;
            batmanBegins.imdbScore = 8.3f;
            batmanBegins.metacriticScore = 0.70f;
            database.create(batmanBegins);

            Movie theDarkKnight = new Movie();
            theDarkKnight.title = "The Dark Knight";
            theDarkKnight.description = "Lorem Ipsum ...";
            theDarkKnight.releaseYear = 2008;
            theDarkKnight.imdbScore = 9.0f;
            theDarkKnight.metacriticScore = 0.82f;
            database.create(theDarkKnight);

            Movie theDarkKnightRises = new Movie();
            theDarkKnightRises.title = "The Dark Knight Rises";
            theDarkKnightRises.description = "Lorem Ipsum ...";
            theDarkKnightRises.releaseYear = 2012;
            theDarkKnightRises.imdbScore = 8.5f;
            theDarkKnightRises.metacriticScore = 0.78f;
            database.create(theDarkKnightRises);

        }

    }

    @RestController
    @RequestMapping("/movies")
    public static class MoviesController {

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
            Link selfLink = linkTo(methodOn(MoviesController.class).get(persistedMovie.id)).withSelfRel();
            return new Resource<>(persistedMovie.movie, selfLink);
        }

    }

    @Service
    public static class MovieDatabase {

        private final Map<UUID, Movie> movies = new HashMap<>();

        public PersistedMovie create(Movie movie) {
            UUID id = UUID.randomUUID();
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

    public static class Movie {
        public String title;
        public String description;
        public Integer releaseYear;
        public Float imdbScore;
        public Float metacriticScore;
    }

    public static class PersistedMovie {
        public final UUID id;
        public final Movie movie;

        public PersistedMovie(UUID id, Movie movie) {
            this.id = id;
            this.movie = movie;
        }
    }

    public static class NotFoundException extends RuntimeException {

    }

}
