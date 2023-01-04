package com.example.apigateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(5)
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(5))
                        .automaticTransitionFromOpenToHalfOpenEnabled(true)
                        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED).build())
                .build()
        );
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route(p-> p
                        .path("/orders")
                        .filters(f-> f.circuitBreaker(c->c.setName("catalog-circuitbreaker1")
                                .setFallbackUri("forward:/fallBackForCatalog")))
                        .uri("lb://CATALOG-SERVICE")
                )
                .route(p-> p
                        .path("/orders/testing/api")
                        .filters(f-> f.circuitBreaker(c->c.setName("catalog-circuitbreaker2")
                                .setFallbackUri("forward:/fallBackForCatalogtesting")))
                        .uri("lb://CATALOG-SERVICE")
                )
                .build();
    }

}
