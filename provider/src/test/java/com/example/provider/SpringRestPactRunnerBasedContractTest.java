package com.example.provider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import java.util.Optional;
import java.util.UUID;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.provider.core.Movie;
import com.example.provider.core.MovieDataStore;
import com.example.provider.core.MovieRecord;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.VerificationReports;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;


@RunWith(SpringRestPactRunner.class)
@Provider("provider")
@PactFolder("src/test/pacts")
@VerificationReports("console")
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class SpringRestPactRunnerBasedContractTest {

    @TestTarget
    public Target target = new HttpTarget("http", "localhost", 8080, "/", true);

    @MockBean
    MovieDataStore dataStore;

    @State("Getting movie with any ID returns Iron Man")
    public void anyMovieByIdRequestReturnsIronMan() {
        Movie ironMan = new Movie();
        ironMan.setTitle("Iron Man");
        ironMan.setDescription("Lorem Ipsum ...");
        ironMan.setReleaseYear(2008);
        ironMan.setImdbScore(7.9f);
        ironMan.setMetacriticScore(0.79f);

        MovieRecord ironManRecord = new MovieRecord(UUID.randomUUID(), ironMan);
        when(dataStore.getById(any())).thenReturn(Optional.of(ironManRecord));
    }

}