package com.example.webfluxdemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;

class ReactorTests {

    @Test
    void verifyFlux() {
        String[] fruits = {"apple", "pear", "orange", "banana", "peach"};
        Flux<String> fruitsFlux = Flux.fromArray(fruits)
                .map(String::toUpperCase)
                .filter(str -> str.length() > 4);

        StepVerifier.create(fruitsFlux)
                .expectNext("APPLE")
                .expectNextMatches(str -> str.startsWith("ORA"))
                .expectNext("BANANA", "PEACH")
                .expectComplete()
                .verify();
    }

    @Test
    void verifyInterval() {
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(1L)).take(3L))
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1L))
                .expectNext(0L)
                .thenAwait(Duration.ofSeconds(1L))
                .expectNext(1L)
                .thenAwait(Duration.ofSeconds(1L))
                .expectNext(2L)
                .expectComplete()
                .verify();
    }

    @Test
    void verifyFlatMap() {
        Flux<Integer> integerFlux = Flux.range(0, 5)
                .flatMap(num -> Mono.just(num).map(number -> number * 10 - 1))
                .subscribeOn(Schedulers.parallel());

        StepVerifier.create(integerFlux)
                .expectNext(-1)
                .expectNext(9)
                .expectNext(19)
                .expectNext(29)
                .expectNext(39)
                .expectComplete()
                .verify();
    }

    @Test
    void verifyDrop() {
        Flux<Integer> integerFlux = Flux.create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.complete();
            sink.next(3);
        });

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectComplete()
                .verifyThenAssertThat()
                .hasDroppedElements()
                .hasDropped(3);
    }

    @Test
    void verifyTestPublisher() {
        TestPublisher<String> testPublisher = TestPublisher.create();
        testPublisher.assertNoSubscribers();
        testPublisher.assertNotCancelled();

        // next ? emit ?
        StepVerifier.create(testPublisher.flux())
                .then(() -> {
                    testPublisher.assertWasSubscribed();
                    testPublisher.next("zero");
                    testPublisher.emit("first", "second", "third");
                    testPublisher.next("fourth");})
                .expectNext("zero", "first", "second", "third")
                .verifyComplete();

        testPublisher.assertWasSubscribed();
    }
}
