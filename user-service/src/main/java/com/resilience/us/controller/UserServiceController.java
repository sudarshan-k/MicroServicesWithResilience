package com.resilience.us.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resilience.us.dto.OrderDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/user-service")
@Slf4j
public class UserServiceController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public static final String USER_SERVICE = "userService";

    private static final String BASEURL = "http://CATALOG-SERVICE/orders";

    private int attempt = 1;


    @GetMapping("/displayOrders")
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getAllAvailableProducts")
//    @Retry(name = USER_SERVICE, fallbackMethod = "getAllAvailableProducts")
    public List<OrderDTO> displayOrders(@RequestParam("category") String category) throws JsonProcessingException {
        String url = category == null ? BASEURL : BASEURL + "/" + category;
        log.info("Url :" + url);
//        System.out.println("retry method called " + attempt++ + " times " + " at " + new Date());
        Flux<OrderDTO> output = webClientBuilder.build().get().uri(url).retrieve().bodyToFlux(OrderDTO.class);
        List<OrderDTO> list = output.collectList().block();
        log.info("Response 1111:"+list);
        return list;
    }


    public List<OrderDTO> getAllAvailableProducts(Exception e) {
        log.info("Fallback method is executed");
        return Stream.of(
                new OrderDTO(119, "LED TV", "electronics", "white", 45000),
                new OrderDTO(345, "Headset", "electronics", "black", 7000),
                new OrderDTO(475, "Sound bar", "electronics", "black", 13000),
                new OrderDTO(574, "Puma Shoes", "foot wear", "black & white", 4600),
                new OrderDTO(678, "Vegetable chopper", "kitchen", "blue", 999),
                new OrderDTO(532, "Oven Gloves", "kitchen", "gray", 745)
        ).collect(Collectors.toList());
    }
}
