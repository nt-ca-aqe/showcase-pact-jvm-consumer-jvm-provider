package com.example.consumertwo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ApplicationLogic implements CommandLineRunner {

    private final MoviesServiceGateway gateway;

    public ApplicationLogic(MoviesServiceGateway gateway) {
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
