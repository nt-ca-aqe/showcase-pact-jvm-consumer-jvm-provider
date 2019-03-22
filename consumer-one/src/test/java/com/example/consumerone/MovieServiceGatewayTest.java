package com.example.consumerone;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.client.RestTemplate;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactTestRun;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.model.MockProviderConfig;
import au.com.dius.pact.model.RequestResponsePact;


public class MovieServiceGatewayTest {

    Map<String, String> headers = Collections.singletonMap("Content-Type", MediaTypes.HAL_JSON_VALUE);

    RestTemplate restTemplate = Application.createCustomRestTemplate();
    MoviesServiceSettings config = new MoviesServiceSettings();

    MoviesServiceGateway cut = new MoviesServiceGateway(restTemplate, config);

    @Test
    public void getSingleMovie() {

        RequestResponsePact pact = ConsumerPactBuilder//
            .consumer("consumer-one")//
            .hasPactWith("provider")//
            .given("Getting movie with any ID returns Iron Man")//
            .uponReceiving("get single movie")//
            .path("/movies/b3fc0be8-463e-4875-9629-67921a1e00f4")//
            .method("GET")//
            .willRespondWith()//
            .status(200)//
            .headers(headers)//
            .body(new PactDslJsonBody()//
                .stringType("title", "Iron Man")//
                .numberType("imdbScore", 7.9f))//
            .toPact();

        executeWithPact(pact, mockServer -> {
            config.setUrl(mockServer.getUrl());
            Movie movie = cut.getMovie("b3fc0be8-463e-4875-9629-67921a1e00f4").get();
            assertThat(movie.getTitle()).isEqualTo("Iron Man");
            assertThat(movie.getImdbScore()).isEqualTo(7.9f);
        });

    }

    @Test
    public void getMoviesByScore() {

    	float minScore = 8.0f;

        DslPart response = new PactDslJsonBody()//
			.eachLike("movies")
				.stringType("title", "Iron Man")//
				.numberType("imdbScore", minScore)
			.closeObject();

		RequestResponsePact pact = ConsumerPactBuilder//
            .consumer("consumer-one")//
            .hasPactWith("provider")//
            .given("Getting movies by score returns at least one movie")//
            .uponReceiving("get movies by score")//
            	.path("/movies")//
            	.query("score=" + minScore)
            	.method("GET")//
            .willRespondWith()//
            	.status(200)//
            	.headers(headers)//
            	.body(response)//
            .toPact();

        executeWithPact(pact, mockServer -> {
            config.setUrl(mockServer.getUrl());
            List<Movie> movies = cut.getMoviesByScore(minScore);
            movies.forEach(movie -> {
            	assertThat(movie.getImdbScore()).isGreaterThanOrEqualTo(minScore);
            });
        });
    }

    private void executeWithPact(RequestResponsePact pact, PactTestRun test) {
        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult result = runConsumerTest(pact, config, test);
        if (result instanceof PactVerificationResult.Error) {
            throw new AssertionError((( PactVerificationResult.Error ) result).getError());
        }
        assertThat(result).isEqualTo(PactVerificationResult.Ok.INSTANCE);
    }

}
