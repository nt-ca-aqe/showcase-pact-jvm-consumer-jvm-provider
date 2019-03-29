package com.example.consumerone;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.client.RestTemplate;

import au.com.dius.pact.consumer.PactTestRun;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.model.MockProviderConfig;
import au.com.dius.pact.model.RequestResponsePact;


public class MovieServiceGatewayTest {

    Map<String, String> headers = Collections.singletonMap("Content-Type", MediaTypes.HAL_JSON_VALUE);

    RestTemplate restTemplate = Application.createCustomRestTemplate();
    MoviesServiceSettings config = new MoviesServiceSettings();

    MoviesServiceGateway cut = new MoviesServiceGateway(restTemplate, config);

    @Test
    public void getSingleMovie() {
        executeWithPact(null, mockServer -> {
            config.setUrl(mockServer.getUrl());
        });

    }

    @Test
    public void getMoviesByScore() {
        executeWithPact(null, mockServer -> {
            config.setUrl(mockServer.getUrl());
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
