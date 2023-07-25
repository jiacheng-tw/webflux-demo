package com.example.webfluxdemo;

import com.example.webfluxdemo.user.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class MonoTests {

    private Mono<Integer> mono = Mono.just(1);

    @Test
    void commandVsReactive() {
        String hello = "hello";
        String helloUp = hello.toUpperCase();
        String message = String.format("%s, %s!", helloUp, hello);
        System.out.println(message);

        Mono.just(hello)
                .map(String::toUpperCase)
                .map(str -> String.format("%s, %s!", str, hello))
                .subscribe(System.out::println);
    }

    @Test
    void createMono() {
        User data = new User("user", "pass");

        Mono.empty().subscribe(System.out::println);

        Mono.never().subscribe(System.out::println);

        Mono.just(data).subscribe(System.out::println);

        Mono.create(monoSink -> monoSink.success(data)).subscribe(System.out::println);

        Mono.defer(() -> Mono.just(Math.random())).subscribe(System.out::println);

        Mono.delay(Duration.ofSeconds(1L)).subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        List<Mono<Integer>> monos = List.of(Mono.empty(), Mono.just(2), Mono.just(3));
        Mono.firstWithValue(monos).subscribe(System.out::println);
        Mono.firstWithSignal(monos).subscribe(System.out::println);

        Mono.zip(Mono.just(1), Mono.just(2), Mono.just(3)).subscribe(System.out::println);
    }

    @Test
    void transferMono() {
        Mono<Integer> mono2 = Mono.just(2);

        // ???
        Mono<Void> and = mono.and(mono2);

        Integer as1 = mono.as(Mono::block);
        assertThat(as1).isOne();
        Optional<Integer> as2 = mono2.as(Mono::blockOptional);
        assertThat(as2.orElse(0)).isEqualTo(2);

        Mono<Number> cast = mono.cast(Number.class);
        assertThat(cast.block()).isInstanceOf(Number.class);
    }

    @Test
    void monoCache() {
        // cache???
        mono.cache().subscribe(System.out::println);
        mono.cacheInvalidateIf(e -> e < 10).subscribe(System.out::println);
        mono.cacheInvalidateWhen(e -> e < 10 ? Mono.empty() : Mono.never()).subscribe(System.out::println);
    }

    @Test
    void monoCheck() {
        Mono<Integer> empty = Mono.empty();

        Mono<Integer> mono1 = empty.defaultIfEmpty(2);
        assertThat(mono1.block()).isEqualTo(2);

        Mono<Tuple2<Long, Integer>> mono2 = mono1.elapsed();
        Tuple2<Long, Integer> tuple = mono2.block();
        assertThat(tuple.getT1()).isZero();
        assertThat(tuple.getT2()).isEqualTo(2);
        mono2.subscribe(System.out::println);

        Mono<Integer> mono3 = empty.or(Mono.just(3));
        assertThat(mono3.hasElement().block()).isFalse();

        Mono<Integer> mono4 = empty.switchIfEmpty(Mono.just(4));
        assertThat(mono4.block()).isEqualTo(4);
    }

    @Test
    void monoDelay() {
        // delaySubscription, delayUntil
        Mono<Integer> mono1 = mono.delayElement(Duration.ofSeconds(1L));
        mono1.subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    void monoFilter() {
        Mono<Integer> mono1 = mono.filter(e -> e > 0);
        assertThat(mono1.block()).isOne();
        Mono<Integer> mono2 = mono.filter(e -> e < 0);
        assertThat(mono2.hasElement().block()).isFalse();
    }

    @Test
    void monoMap() {
        Mono<Integer> mono1 = mono.map(e -> e * 10);
        assertThat(mono1.block()).isEqualTo(10);
        Mono<Integer> mono2 = mono.flatMap(e -> Mono.just(e * 10));
        assertThat(mono2.block()).isEqualTo(10);
    }

    @Test
    void monoZip() {
        Mono<Tuple2<Integer, Integer>> mono1 = mono.zipWhen(e -> Mono.just(e + 1));
        mono1.subscribe(System.out::println);

        Mono<Integer> mono2 = mono.zipWhen(e -> Mono.just(e + 1), Integer::sum);
        mono2.subscribe(System.out::println);

        Mono<Tuple2<Integer, Integer>> mono3 = mono.zipWith(mono);
        mono3.subscribe(System.out::println);

        Mono<Integer> mono4 = mono.zipWith(mono, Integer::sum);
        mono4.subscribe(System.out::println);
    }

    @Test
    void monoDoSomething() {

    }

    @Test
    void monoOnSomething() {

    }

    @Test
    void monoExpand() {

    }

    @Test
    void monoLog() {

    }

    @Test
    void monoPublish() {
        
    }

    @Test
    void monoSubscribe() {

    }

    @Test
    void monoRepeat() {

    }

    @Test
    void monoRetry() {

    }

    @Test
    void monoTake() {

    }

    @Test
    void monoThen() {

    }

    @Test
    void monoTime() {

    }
}
