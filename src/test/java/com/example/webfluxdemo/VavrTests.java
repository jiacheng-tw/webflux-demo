package com.example.webfluxdemo;

import com.example.webfluxdemo.user.User;
import com.example.webfluxdemo.user.UserValidator;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple0;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.run;
import static org.assertj.core.api.Assertions.assertThat;

class VavrTests {

    private static final String HELLO = "Hello";
    private static final String NULL = "Null";

    @Test
    void optionTest() {
        Option<Object> nullOption = Option.of(null);
        Option<String> helloOption = Option.of(HELLO);
        Option<String> someOption = Option.some(HELLO);

        assertThat(nullOption.isEmpty()).isTrue();
        assertThat(nullOption.getOrElse(NULL)).isEqualTo(NULL);
        assertThat(helloOption.getOrElse(NULL)).isEqualTo(HELLO);
        assertThat(someOption.getOrElseThrow(RuntimeException::new)).isEqualTo(HELLO);
    }

    @Test
    void tupleTest() {
        Tuple0 emptyTuple = Tuple.empty();
        Tuple3<Integer, Long, String> threeTuple = Tuple.of(1, 2L, HELLO);

        assertThat(emptyTuple.toString()).hasToString("()");
        assertThat(threeTuple._1).isEqualTo(1);
        assertThat(threeTuple._2).isEqualTo(2L);
        assertThat(threeTuple._3).isEqualTo(HELLO);

        Tuple4<Integer, Long, String, String> fourTuple = threeTuple.append(NULL);
        assertThat(fourTuple._4()).isEqualTo(NULL);
    }

    @Test
    void tryTest() {
        Try<Integer> result = Try.of(() -> 1 / 0);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getOrElse(-1)).isEqualTo(-1);
    }

    @Test
    void validationTest() {
        UserValidator userValidator = new UserValidator();

        User user = new User("username", "password");
        Validation<Seq<String>, User> validation = userValidator.userValidation(user);

        user.setPass("p@ssword");
        Validation<Seq<String>, User> invalidation = userValidator.userValidation(user);

        assertThat(validation.isValid()).isTrue();
        assertThat(invalidation.isValid()).isFalse();
    }

    @Test
    void lazyTest() {
        Lazy<Double> lazyNumber = Lazy.of(Math::random);
        assertThat(lazyNumber.isEvaluated()).isFalse();

        Double result1 = lazyNumber.getOrNull();
        assertThat(lazyNumber.isEvaluated()).isTrue();

        Double result2 = lazyNumber.getOrNull();
        assertThat(result1).isEqualTo(result2);
    }

    @Test
    void matchTest() {
        Tuple4<BigDecimal, Integer, Long, String> tuple = Tuple.of(BigDecimal.ONE, 2, 3L, "4");

        for (int i = 1; i < 5; i++) {
            Match(i).of(
                    Case($(1), () -> run(() -> System.out.println(tuple._1))),
                    Case($(2), () -> run(() -> System.out.println(tuple._2))),
                    Case($(3), () -> run(() -> System.out.println(tuple._3))),
                    Case($(4), () -> run(() -> System.out.println(tuple._4)))
            );
        }
    }
}
