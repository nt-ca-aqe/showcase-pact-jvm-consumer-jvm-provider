package com.example.provider;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.runner.RunWith;
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

    @State("Getting movies by score returns at least one movie")
    public void gettingMoviesByScoreReturnsAtLeastOneMovie() {
        Movie lotr = new Movie();
        lotr.setTitle("The Lord of the Rings - The Fellowship of the Ring");
        lotr.setDescription("foo bar baz");
        lotr.setReleaseYear(2001);
        lotr.setImdbScore(9.2f);

        Movie sw = new Movie();
        sw.setTitle("Star Wars The Empre Strikes Back");
        sw.setDescription("abc");
        sw.setReleaseYear(1980);
        sw.setImdbScore(8.2f);

        MovieRecord lotrRecord = new MovieRecord(UUID.randomUUID(), lotr);
        MovieRecord swRecord = new MovieRecord(UUID.randomUUID(), sw);

        when(dataStore.getAll()).thenReturn(new HashSet<>(asList(lotrRecord, swRecord)));
    }


}
