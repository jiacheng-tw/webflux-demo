package com.example.webfluxdemo.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRouter {

    @Bean
    public WebClient webClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    public RouterFunction<ServerResponse> routeUser(UserHandler userHandler) {
        return RouterFunctions
                .route(GET("/user/hello").and(accept(MediaType.TEXT_PLAIN)), userHandler::helloUser)
                .andRoute(GET("/user/info/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUserInfo)
                .andRoute(GET("/user/forward").and(accept(MediaType.APPLICATION_JSON)), userHandler::forwardUserInfo)
                .andRoute(POST("/user/info").and(accept(MediaType.APPLICATION_JSON)), userHandler::postUserInfo);
    }
}
