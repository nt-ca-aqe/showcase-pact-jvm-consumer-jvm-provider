package com.example.consumerone;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@ComponentScan
@SpringBootApplication
@EnableConfigurationProperties
public class ConsumerOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerOneApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        // Without spring boot starter dependencies on "web" and "hateoas", the default RestTemplate must be configured:
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(objectMapper);

        List<HttpMessageConverter<?>> messageConverters = singletonList(converter);
        return new RestTemplate(messageConverters);
    }

    @Component
    public static class ConsumerLogic implements CommandLineRunner {

        private final MoviesServiceGateway gateway;

        public ConsumerLogic(MoviesServiceGateway gateway) {
            this.gateway = gateway;
        }

        @Override
        public void run(String... args) throws Exception {
            System.out.println();
            System.out.println("Received the following movies from the producer:");
            gateway.getMovies().forEach(movie -> System.out.println(" - " + movie));
            System.out.println();
        }

    }

    @Service
    public static class MoviesServiceGateway {

        private final RestTemplate restTemplate;
        private final MovieServiceConfig config;

        public MoviesServiceGateway(RestTemplate restTemplate, MovieServiceConfig config) {
            this.restTemplate = restTemplate;
            this.config = config;
        }

        public List<Movie> getMovies() {
            ResponseEntity<MovieList> response =
                restTemplate.exchange(config.getUrl() + "/movies", HttpMethod.GET, HttpEntity.EMPTY, MovieList.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Resources<Resource<Movie>> responseBody = response.getBody();
                return responseBody.getContent().stream()//
                    .map(Resource::getContent)//
                    .collect(Collectors.toList());
            }
            throw new IllegalStateException("server responded with: " + response);
        }

    }

    public static class MovieList extends Resources<Resource<Movie>> {

    }

    @Component
    @ConfigurationProperties("services.movies")
    public static class MovieServiceConfig {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Movie {

        private String title;
        private Float imdbScore;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Float getImdbScore() {
            return imdbScore;
        }

        public void setImdbScore(Float imdbScore) {
            this.imdbScore = imdbScore;
        }

        @Override
        public String toString() {
            return "Movie{" + "title='" + title + '\'' + ", imdbScore=" + imdbScore + '}';
        }

    }

}
