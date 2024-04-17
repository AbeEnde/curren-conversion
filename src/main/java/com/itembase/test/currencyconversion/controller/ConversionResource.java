package com.itembase.test.currencyconversion.controller;

import com.itembase.test.currencyconversion.dto.ConversionRequest;
import com.itembase.test.currencyconversion.dto.ConversionResponse;
import com.itembase.test.currencyconversion.service.ConversionService;
//import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ConversionResource {
    @Autowired
    ConversionService conversionService;

    @PostMapping("/currency/convert")
    public Mono<ConversionResponse> convert(@RequestBody ConversionRequest request) {
        return conversionService.convert(request).flatMap(Mono::just);
    }
}
