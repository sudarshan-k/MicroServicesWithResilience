package com.example.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class FallBackMethodController {

    @GetMapping("/fallBack")
    public String fallBackMethod() {
        return "Fallback method from api gateway.................";

    }

   /* @GetMapping("/siteLookupFallBack")
    public ResponseEntity<FallbackResponse> siteLookupFallBackMethod(){
        FallbackResponse response = FallbackResponse.builder()
                .status("error")
                .errors(Collections.singletonList(Error.builder()
                        .errorCode(HttpStatus.GATEWAY_TIMEOUT.value())
                        .errorDesc("SMS Site Lookup Service is taking longer than expected.Please try again later.")
                        .build()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.GATEWAY_TIMEOUT);
    }*/

    @GetMapping("/fallBackForCatalog")
    public String fallBackMethodCatalog() {
        return "Fallback method from api gateway for catalog service for no pathvariable.................";

    }

    @GetMapping("/fallBackForCatalogtesting")
    public String fallBackMethodCatalogPath() {
        return "Fallback method from api gateway for catalog service for testing  .................";

    }
}
