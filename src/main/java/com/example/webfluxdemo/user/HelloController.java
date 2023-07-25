package com.example.webfluxdemo.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/mono")
    public Mono<String> helloMono() {
        return Mono.just("Hello Mono!");
    }

    @GetMapping("/flux")
    public Flux<String> helloFlux() {
        return Flux.just("Hello", "Flux", "!");
    }

    @GetMapping("/users")
    public Flux<User> helloUser() {
        return Flux.just(new User("1", "1"), new User("2", "2"));
    }
}
