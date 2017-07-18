package com.example.consumertwo;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.client.RestTemplate;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactTestRun;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.model.MockProviderConfig;
import au.com.dius.pact.model.RequestResponsePact;


public class MovieServiceGatewayTest {

    Map<String, String> headers = Collections.singletonMap("Content-Type", MediaTypes.HAL_JSON_VALUE);

    RestTemplate restTemplate = Application.createCustomRestTemplate();
    MoviesServiceConfig config = new MoviesServiceConfig();

    MoviesServiceGateway cut = new MoviesServiceGateway(restTemplate, config);

    @Test
    public void getSingleMovie() throws Throwable {

        RequestResponsePact pact = ConsumerPactBuilder//
            .consumer("consumer-two")//
            .hasPactWith("producer")//
            .given("Movie with ID 'b3fc0be8-463e-4875-9629-67921a1e00f4' exists")//
            .uponReceiving("get single movie")//
            .path("/movies/b3fc0be8-463e-4875-9629-67921a1e00f4")//
            .method("GET")//
            .willRespondWith()//
            .status(200)//
            .headers(headers)//
            .body(new PactDslJsonBody()//
                .stringType("title", "Iron Man")//
                .numberType("releaseYear", 2008)//
                .numberType("metacriticScore", 0.79f)//
                .object("_links")//
                .object("self")//
                .stringType("href", "http://some-url/resource")//
                .closeObject()//
                .closeObject())//
            .toPact();

        executeWithPact(pact, mockServer -> {
            config.setUrl(mockServer.getUrl());
            Movie movie = cut.getMovie("b3fc0be8-463e-4875-9629-67921a1e00f4").get();
            assertThat(movie.getTitle()).isEqualTo("Iron Man");
            assertThat(movie.getReleaseYear()).isEqualTo(2008);
            assertThat(movie.getMetacriticScore()).isEqualTo(0.79f);
            assertThat(movie.getId()).isNotNull();
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
