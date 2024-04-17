package com.itembase.test.currencyconversion.service;

import com.itembase.test.currencyconversion.dto.ConversionRequest;
import com.itembase.test.currencyconversion.dto.ConversionResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ConversionService {
    private static final Logger logger = LoggerFactory.getLogger(ConversionService.class);

    @Value("${external.api1}")
    private String externalApi1;
    @Value("${external.api2}")
    private String externalApi2;

    private final WebClient webClient;

    public ConversionService() {
        this.webClient =  WebClient.create();
    }


    /**
     * Make api requests to two endpoints based on their rank from decideUrl() method
     * if the first fails it makes a call to the second.
     * @param fromCurrency
     * @return
     */
    public Mono<String> fetchRate(String fromCurrency) {
        return decideUrl().flatMap(map -> {
            String firstUrl = map.get("first")+fromCurrency;
            String secondUrl = map.get("second")+fromCurrency;
            return webClient.get()
                    .uri(firstUrl)
                    .retrieve()
                    .toEntity(String.class)
                    .flatMap(responseEntity -> {
                        HttpStatus statusCode = responseEntity.getStatusCode();
                        if (statusCode.is2xxSuccessful()) {
                            return Mono.just(responseEntity.getBody());
                        } else {
                            return webClient.get()
                                    .uri(secondUrl)
                                    .retrieve()
                                    .toEntity(String.class)
                                    .flatMap(secondResponseEntity -> {
                                        if(secondResponseEntity.getStatusCode().is2xxSuccessful()) {
                                            return Mono.just(secondResponseEntity.getBody());
                                        } else{
                                            return Mono.just("There is no online Provider, please try later. ");
                                        }
                                    });
                        }
                    })
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        throw new RuntimeException("system error", e);
                    });
        });
    }

    /**
     * a method responsible for converting incoming amount fro base currency to the target currency
     * it fetches conversion rate from fetchRate()
     * @param request
     * @return
     */
    public Mono<ConversionResponse> convert(ConversionRequest request) {
        return fetchRate(request.getFrom())
                .flatMap(rateResponse -> {
                    JSONObject jsonObject = new JSONObject(rateResponse);
                    JSONObject ratesObject = jsonObject.getJSONObject("rates");
                    Double rate = ratesObject.getDouble(request.getTo());
                    ConversionResponse response = new ConversionResponse();
                    response.setAmount(request.getAmount());
                    response.setTo(request.getTo());
                    response.setFrom(request.getFrom());
                    response.setConverted(request.getAmount()* rate);
                    return Mono.just(response);
                });

    }

    /**
     * Randomly decide the url and send by labeling first and second
     * @return Mono of randomizedUrl
     */
    private Mono<Map<String, String>> decideUrl(){
        List<String> urls = new java.util.ArrayList<>(List.of(externalApi1, externalApi2));
        Random random = new Random();
        Map<String, String>randomizedUrl = new HashMap<String, String>();
        randomizedUrl.put("first",urls.remove(random.nextInt(urls.size())));
        randomizedUrl.put("second", urls.get(0));
        return Mono.just(randomizedUrl);
    }
}
