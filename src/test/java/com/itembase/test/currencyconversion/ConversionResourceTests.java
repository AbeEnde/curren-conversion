package com.itembase.test.currencyconversion;

import com.itembase.test.currencyconversion.dto.ConversionRequest;
import com.itembase.test.currencyconversion.dto.ConversionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureWebTestClient
public class ConversionResourceTests {
    @Autowired
    private WebTestClient webTestClient;
    @Test
     void convert() {
        ConversionRequest request = new ConversionRequest();
        request.setFrom("EUR");
        request.setTo("USD");
        request.setAmount(10.4);

        webTestClient.post()
                .uri("/api/currency/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ConversionRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConversionResponse.class)
                .consumeWith(response -> {
                    ConversionResponse responseBody = response.getResponseBody();
                    assert responseBody != null;
                    assertEquals("EUR", responseBody.getFrom());
                    assertEquals("USD", responseBody.getTo());
                    assertTrue(10.4 < responseBody.getConverted());
                });
    }
}
