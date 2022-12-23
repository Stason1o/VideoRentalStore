package com.rentalstore.service.calculator;

import com.rentalstore.dto.request.FilmRentRequest;
import com.rentalstore.dto.request.FilmReturnSummary;
import com.rentalstore.exceptions.NegativeValueException;
import com.rentalstore.exceptions.RentCalculationException;
import com.rentalstore.model.Film;
import com.rentalstore.model.Type;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FilmRentCalculatorTest {

    private final FilmRentCalculator calculator = new FilmRentCalculator();

    static Stream<Arguments> generateTestPairsForRent() {
        var firstPairs = asList(
                new ImmutablePair<>(new Film("Film 1", Type.NEW), new FilmRentRequest("Film 1", 3)),
                new ImmutablePair<>(new Film("Film 2", Type.OLD), new FilmRentRequest("Film 2", 5)),
                new ImmutablePair<>(new Film("Film 3", Type.REGULAR), new FilmRentRequest("Film 3", 4))
        );
        var secondPairs = asList(
                new ImmutablePair<>(new Film("Film 1", Type.NEW), new FilmRentRequest("Film 1", 1)),
                new ImmutablePair<>(new Film("Film 2", Type.OLD), new FilmRentRequest("Film 2", 3)),
                new ImmutablePair<>(new Film("Film 3", Type.REGULAR), new FilmRentRequest("Film 3", 2))
        );
        return Stream.of(
                Arguments.of(firstPairs, 210),
                Arguments.of(secondPairs, 100)
        );
    }

    static Stream<Arguments> generateTestPairsForReturnPriceCalculation() {
        var firstPairs = asList(
                new ImmutablePair<>(new Film("Film 1", Type.NEW), new FilmReturnSummary("Film 1", 3)),
                new ImmutablePair<>(new Film("Film 2", Type.OLD), new FilmReturnSummary("Film 2", 5)),
                new ImmutablePair<>(new Film("Film 3", Type.REGULAR), new FilmReturnSummary("Film 3", 4))
        );
        var secondPairs = asList(
                new ImmutablePair<>(new Film("Film 1", Type.NEW), new FilmReturnSummary("Film 1", 1)),
                new ImmutablePair<>(new Film("Film 2", Type.OLD), new FilmReturnSummary("Film 2", 3)),
                new ImmutablePair<>(new Film("Film 3", Type.REGULAR), new FilmReturnSummary("Film 3", 2))
        );
        return Stream.of(
                Arguments.of(firstPairs, 390),
                Arguments.of(secondPairs, 190)
        );
    }

    @ParameterizedTest
    @MethodSource("generateTestPairsForRent")
    void shouldCalculateRentPrice(List<ImmutablePair<Film, FilmRentRequest>> argument, Integer expected) {
        assertThat(calculator.calculateRentPrice(argument)).isEqualTo(expected);
    }

    @Test
    void shouldThrowRentCalculationException() {
        var pairs = new ArrayList<ImmutablePair<Film, FilmRentRequest>>();

        Assertions.assertThatExceptionOfType(RentCalculationException.class)
                .isThrownBy(() -> calculator.calculateRentPrice(pairs));
    }

    @Test
    void shouldThrowNegativeValueException() {
        var pairs = asList(
                new ImmutablePair<>(new Film("Film 1", Type.NEW), new FilmRentRequest("Film 1", -1)),
                new ImmutablePair<>(new Film("Film 2", Type.OLD), new FilmRentRequest("Film 2", 5)),
                new ImmutablePair<>(new Film("Film 3", Type.REGULAR), new FilmRentRequest("Film 3", 4))
        );

        Assertions.assertThatExceptionOfType(NegativeValueException.class)
                .isThrownBy(() -> calculator.calculateRentPrice(pairs));
    }

    @ParameterizedTest
    @MethodSource("generateTestPairsForReturnPriceCalculation")
    void calculateReturnPrice(List<ImmutablePair<Film, FilmReturnSummary>> argument, Integer expected) {
        assertThat(calculator.calculateReturnPrice(argument)).isEqualTo(expected);
    }
}