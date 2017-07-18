package com.example.provider;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;


@RunWith(PactRunner.class)
@Provider("provider")
@PactFolder("src/test/pacts")
@VerificationReports("console")
public class MoviesControllerContractTest {

    static ConfigurableApplicationContext applicationContext;

    @TestTarget
    public Target target = new HttpTarget("http", "localhost", 8080, "/", true);

    @BeforeClass
    public static void startApplication() {
        applicationContext = SpringApplication.run(Application.class);
    }

    @AfterClass
    public static void closeApplication() {
        applicationContext.close();
    }

    @State("Movie with ID 'b3fc0be8-463e-4875-9629-67921a1e00f4' exists")
    public void movieWithSpecificIdExists() {
        MovieDatabase database = applicationContext.getBean(MovieDatabase.class);
        UUID id = UUID.fromString("b3fc0be8-463e-4875-9629-67921a1e00f4");

        Movie ironMan = new Movie();
        ironMan.setTitle("Iron Man");
        ironMan.setDescription("Lorem Ipsum ...");
        ironMan.setReleaseYear(2008);
        ironMan.setImdbScore(7.9f);
        ironMan.setMetacriticScore(0.79f);
        database.createOrUpdate(id, ironMan);
    }

}
