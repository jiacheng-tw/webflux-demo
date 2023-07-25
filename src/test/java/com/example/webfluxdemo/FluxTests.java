package com.example.webfluxdemo;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class FluxTests {

    @Test
    void createFlux() {
        Flux.just(1, 2, 3).subscribe(System.out::println);

        Flux.create(fluxSink -> {
            for (int i = 4; i < 7; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
        }).subscribe(System.out::println);

        Flux.generate(fluxSink -> {
            fluxSink.next(7);
            fluxSink.complete();
        }).subscribe(System.out::println);

        Flux.range(8, 3).subscribe(System.out::println);

        Flux.interval(Duration.ofSeconds(1L)).subscribe(System.out::println);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    void fluxBuffer() {
        Flux<Integer> range = Flux.range(0, 10);

        range.buffer(5).subscribe(System.out::println);

        range.bufferUntil(num -> num % 5 == 0).subscribe(System.out::println);

        range.bufferUntilChanged().subscribe(System.out::println);

        range.bufferWhile(num -> num > 6).subscribe(System.out::println);

        range.window(5).subscribe(flux -> flux.buffer(2).subscribe(System.out::println));
    }

    @Test
    void fluxFilter() {
        Flux<Integer> range = Flux.range(0, 10);

        range.filter(num -> num % 3 == 0).subscribe(System.out::println);

        range.take(3L).subscribe(System.out::println);

        range.takeLast(3).subscribe(System.out::println);

        range.takeUntil(num -> num % 2 == 1).subscribe(System.out::println);

        range.takeWhile(num -> num < 5).subscribe(System.out::println);
    }

    @Test
    void fluxMerge() {
        Flux<Integer> range1 = Flux.range(0, 2);
        Flux<Integer> range2 = Flux.range(2, 2);

        range1.mergeWith(range2).subscribe(System.out::println);

        range1.mergeComparingWith(range2, ((o1, o2) -> o2 - o1)).subscribe(System.out::println);

        range1.zipWith(range2).subscribe(System.out::println);

        range1.flatMap(Mono::just).subscribe(System.out::println);
    }

    @Test
    void fluxReduce() {
        Flux<Integer> range = Flux.range(0, 6);

        range.reduce(Integer::sum).subscribe(System.out::println);

        range.reduceWith(() -> 10, Integer::sum).subscribe(System.out::println);
    }

    @Test
    void fluxCollect() {
        Flux<Integer> range = Flux.range(0, 5);

        Mono<List<Integer>> listMono = range.collectList();
        listMono.subscribe(list ->
                list.forEach(System.out::println));

        Mono<Map<String, Integer>> mapMono = range.collectMap(Object::toString);
        mapMono.subscribe(map ->
                map.forEach((key, value) -> System.out.println(key + " -> " + value)));

        Mono<Double> collect = range.collect(Collectors.averagingInt(num -> num));
        collect.subscribe(System.out::println);
    }

    @Test
    void fluxSubscription() {
        ArrayList<Integer> integers = new ArrayList<>();
        Flux<Integer> integerFlux = Flux.just(1, 2, 3);

        integerFlux.log().subscribe(new Subscriber<>() {
            private Subscription subscription;
            private int onNextAmount;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                s.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                integers.add(integer);
                onNextAmount++;
                if (onNextAmount % 2 == 0) {
                    subscription.request(2);
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println(onNextAmount);
            }
        });

        integerFlux
                .log()
                .doOnSubscribe(s -> s.request(2))
                .doOnNext(integers::add)
                .doOnError(Throwable::printStackTrace)
                .doOnComplete(() -> System.out.println(integers.size()))
                .subscribe();
    }

    @Test
    void fluxHotStreams() {
        Flux.create(fluxSink -> {
            for (int i = 0; i < 10; i++) {
                fluxSink.next(System.currentTimeMillis());
            }
            fluxSink.complete();
        }).log().subscribe();

        Flux.generate(() -> 0, (state, sink) -> {
            sink.next(state + 1);
            if (state > 10) {
                sink.complete();
            }
            return state + 1;
        }).log().subscribe();
    }

    @Test
    void name() {
        Flux<Integer> range = Flux.range(0, 10);
        range.take(-1).subscribe(System.out::println);
    }
}
