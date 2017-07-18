package com.example.provider;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ApplicationSetup implements CommandLineRunner {

    private final MovieDatabase database;

    public ApplicationSetup(MovieDatabase database) {
        this.database = database;
    }

    @Override
    public void run(String... args) throws Exception {

        Movie batmanBegins = new Movie();
        batmanBegins.setTitle("Batman Begins");
        batmanBegins.setDescription("Lorem Ipsum ...");
        batmanBegins.setReleaseYear(2005);
        batmanBegins.setImdbScore(8.3f);
        batmanBegins.setMetacriticScore(0.70f);
        database.create(batmanBegins);

        Movie theDarkKnight = new Movie();
        theDarkKnight.setTitle("The Dark Knight");
        theDarkKnight.setDescription("Lorem Ipsum ...");
        theDarkKnight.setReleaseYear(2008);
        theDarkKnight.setImdbScore(9.0f);
        theDarkKnight.setMetacriticScore(0.82f);
        database.create(theDarkKnight);

        Movie theDarkKnightRises = new Movie();
        theDarkKnightRises.setTitle("The Dark Knight Rises");
        theDarkKnightRises.setDescription("Lorem Ipsum ...");
        theDarkKnightRises.setReleaseYear(2012);
        theDarkKnightRises.setImdbScore(8.5f);
        theDarkKnightRises.setMetacriticScore(0.78f);
        database.create(theDarkKnightRises);

    }

}
