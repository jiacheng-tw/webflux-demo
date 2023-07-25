package com.example.webfluxdemo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(UserHandler.class);

    public UserHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ServerResponse> helloUser(ServerRequest request) {
        logger.info(request.toString());

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue("Hello, user!"));
    }

    public Mono<ServerResponse> getUserInfo(ServerRequest request) {
        logRequest(request);

        String id = request.pathVariable("id");
        String status = request.queryParam("status").orElse("false");
        logger.info("user gotten -> id: {}, status: {}", id, status);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new User("user xxx", "pass xxx")), User.class);
    }

    public Mono<ServerResponse> forwardUserInfo(ServerRequest request) {
        logRequest(request);

        String uri = "/user/info/1";
        Mono<User> userMono = webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, res -> Mono.error(ClassNotFoundException::new))
                .onStatus(HttpStatus::is5xxServerError, res -> Mono.error(IllegalStateException::new))
                .bodyToMono(User.class);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userMono, User.class);
    }

    public Mono<ServerResponse> postUserInfo(ServerRequest request) {
        logRequest(request);

        Mono<User> userMono = request.bodyToMono(User.class);
        // Invalid?
        userMono.subscribe(user -> logger.info("user created -> {}", user));
        // MonoOnErrorResume?
        logger.info("user created -> {}", userMono);

        return ServerResponse.ok().build();
    }

    private void logRequest(ServerRequest request) {
        logger.info("request -> {}", request);
    }
}
