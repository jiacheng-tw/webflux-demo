package com.example.webfluxdemo;

import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Lazy;
import io.vavr.Predicates;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class VavrExampleTests {

    @Test
    void vavrTupleTest() {
        Tuple2<String, Integer> tupleExample1 = Tuple.of("vavr", 1);
        assertThat(tupleExample1._1).isEqualTo("vavr");
        assertThat(tupleExample1._2()).isEqualTo(1);

        Tuple2<String, Integer> tupleExample2 = tupleExample1
                .update1("VAVR")
                .map2(num -> num + 1);
//        Tuple2<String, Integer> tupleExample2 = tupleExample1.map(String::toUpperCase, num -> num + 1);
//        Tuple2<String, Integer> tupleExample2 = tupleExample1.map(
//                (str, num) -> Tuple.of(str.toUpperCase(), num + 1));
        assertThat(tupleExample2._1).isEqualTo("VAVR");
        assertThat(tupleExample2._2()).isEqualTo(2);

        Tuple3<String, Integer, Double> tupleExample3 = tupleExample2.append(0.1);
        String tupleStr3 = tupleExample3.apply(
                (str, intVal, doubleVal) -> String.format("%s %d %.1f", str, intVal, doubleVal));
        assertThat(tupleStr3).isEqualTo("VAVR 2 0.1");

        Tuple4<String, Integer, String, Integer> tupleExample4 = tupleExample1.concat(tupleExample2);
        String tupleStr4 = tupleExample4.apply(
                (str1, num1, str2, num2) -> String.format("%s %d %s %d", str1, num1, str2, num2));
        assertThat(tupleStr4).isEqualTo("vavr 1 VAVR 2");
    }

    @Test
    void vavrFunctionCompositionTest() {
        Function1<Integer, String> numToStr = String::valueOf;
        assertThat(numToStr.apply(100))
                .isEqualTo("100")
                .isInstanceOf(String.class);

        Function1<String, Double> strToNum = Double::valueOf;
        assertThat(strToNum.apply("100"))
                .isEqualTo(100.0)
                .isInstanceOf(Double.class);

        Function1<Integer, Double> integerToDouble = numToStr.andThen(strToNum);
//        Function1<Integer, Double> integerToDouble = strToNum.compose(numToStr);
        assertThat(integerToDouble.apply(100))
                .isEqualTo(100.0)
                .isInstanceOf(Double.class);
    }

    @Test
    void vavrFunctionLiftingTest() {
        Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        assertThat(divide.apply(2, 1)).isEqualTo(2);
        catchException(() -> divide.apply(2, 0)).printStackTrace();

        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);
        // Partial Application
        assertThat(safeDivide.apply(2).apply(1)).isEqualTo(Option.of(2));
        // Currying
        assertThat(safeDivide.curried().apply(2).apply(0)).isEqualTo(Option.none());
    }

    @Test
    void vavrFunctionMemoizationTest() {
        Function0<Double> memoizedRandom = Function0.of(Math::random).memoized();
        assertThat(memoizedRandom.isMemoized()).isTrue();

        Double memoizedValue = memoizedRandom.apply();
        assertThat(memoizedRandom.apply()).isEqualTo(memoizedValue);
        assertThat(memoizedRandom.apply()).isEqualTo(memoizedValue);
        // function type need be declared before memoized
//        Function1<Integer, Double> memoized = Function1.of(num -> Math.random() * num).memoized();
        Function1<Integer, Double> randomProduct = Function1.of(num -> Math.random() * num);
        Function1<Integer, Double> memoizedRandomProduct = randomProduct.memoized();
        assertThat(memoizedRandomProduct.apply(2)).isEqualTo(memoizedRandomProduct.apply(2));
    }

    @Test
    void vavrOptionTest() {
        Optional<String> optional = Optional.of("hello");
        Optional<String> emptyOptional = optional
                .map(str -> null);
        assertThat(emptyOptional).isEmpty();

        Optional<String> optionalResult = emptyOptional
                .map(String::toUpperCase);
        assertThat(optionalResult).isEmpty();

        Option<String> option = Option.of("hello");
        Option<String> emptyOption = option
                .map(str -> null);
        assertThat(emptyOption).isNotEmpty();

        Option<String> optionResult = emptyOption
                .flatMap(Option::of)
                // It would throw NullPointerException without null value handle.
                .map(String::toUpperCase);
        assertThat(optionResult).isEmpty();
    }

    @Test
    void vavrTryTest() {
        Try<Integer> normal = Try.of(() -> 1 + 1);
        assertThat(normal.isSuccess()).isTrue();
        assertThat(normal.get()).isEqualTo(2);

        Try<Integer> error = Try.of(() -> 1 / 0);
        assertThat(error.isFailure()).isTrue();
        assertThat(error.getOrElse(0)).isZero();
        Try<Integer> recoverValue = error.recover(ArithmeticException.class, 1);
        assertThat(recoverValue.isSuccess()).isTrue();
        assertThat(recoverValue.get()).isOne();
    }

    @Test
    void vavrLazyTest() {
        Lazy<Double> lazyRandom = Lazy.of(Math::random);
        assertThat(lazyRandom.isEvaluated()).isFalse();

        Double randomResult = lazyRandom.getOrElse(0D);
        assertThat(lazyRandom.isEvaluated()).isTrue();
        assertThat(lazyRandom.get()).isEqualTo(randomResult);
    }

    @Test
    void vavrListTest() {
        List<Integer> javaList = List.of(1, 2, 3);
        catchException(() -> javaList.set(0, 0)).printStackTrace();
        List<Integer> mappedJavaList = javaList
                .stream()
                .filter(num -> num % 2 == 1)
                .map(num -> num + 1)
                .collect(Collectors.toList());
        assertThat(mappedJavaList)
                .hasSize(2)
                .contains(2, 4);

        io.vavr.collection.List<Integer> vavrList = io.vavr.collection.List.of(1, 2, 3);
        io.vavr.collection.List<Integer> updateVavrList = vavrList.update(0, 0);
        assertThat(updateVavrList.get(0)).isZero();
        io.vavr.collection.List<Integer> mappedVavrList = vavrList
                .filter(num -> num % 2 == 1)
                .map(num -> num + 1);
        assertThat(mappedVavrList)
                .hasSize(2)
                .contains(2, 4);
    }

    @Test
    void vavrPatternMatchingTest() {
        String output1 = judgeWithIf(1);
        assertThat(output1).isEqualTo("one");

        String output2 = judgeWithSwitch(2);
        assertThat(output2).isEqualTo("two");

        String output3 = judgeWithMatch(3);
        assertThat(output3).isEqualTo("three");
    }

    private static String judgeWithMatch(int input) {
        return Match(input).of(
                Case($(1), "one"),
                Case($(num -> num == 2), "two"),
                Case($(Predicates.is(3)), () -> "three"),
                Case($(), num -> "other: " + num)
        );
    }

    private static String judgeWithSwitch(int input) {
        switch (input) {
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            default:
                return "other: " + input;
        }
    }

    private static String judgeWithIf(int input) {
        if (input == 1) {
            return "one";
        } else if (input == 2) {
            return "two";
        } else if (input == 3) {
            return "three";
        } else {
            return "other: " + input;
        }
    }
}
