package com.resilience.us;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Paths;

public class WebclientShortCircuit {
    private static WebClient createWebClient(HttpStatus httpStatus, String desiredResponseName) throws Exception {
        final String content = Files.readString(Paths.get("src/test/resources/responses", desiredResponseName + ".json"));
        return WebClient.builder()
                .exchangeFunction(request ->
                        Mono.just(ClientResponse.create(httpStatus)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .body(content)
                                .build()))
                .build();
    }
}
