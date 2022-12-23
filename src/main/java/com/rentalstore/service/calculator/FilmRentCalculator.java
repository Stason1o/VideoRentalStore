package com.rentalstore.service.calculator;

import com.rentalstore.dto.request.FilmRentRequest;
import com.rentalstore.dto.request.FilmReturnRequestOld;
import com.rentalstore.dto.request.FilmReturnSummary;
import com.rentalstore.exceptions.NegativeValueException;
import com.rentalstore.exceptions.RentCalculationException;
import com.rentalstore.model.Film;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmRentCalculator {

    private static Integer calculateSingleFilmReturnPriceOld(ImmutablePair<Film, FilmReturnRequestOld> pair) {
        var exceededDaysOfUse = pair.right.exceededDaysOfUse();
        var defaultRentPrice = pair.left.type().getPriceType().getPrice();

        return basicCalculation(exceededDaysOfUse, defaultRentPrice);
    }

    private static Integer calculateSingleFilmReturnPrice(ImmutablePair<Film, FilmReturnSummary> pair) {
        var exceededDaysOfUse = pair.right.exceededDaysOfUse();
        var defaultRentPrice = pair.left.type().getPriceType().getPrice();

        return basicCalculation(exceededDaysOfUse, defaultRentPrice);
    }

    private static int basicCalculation(Integer exceededDaysOfUse, Integer defaultRentPrice) {
        if (exceededDaysOfUse < 0) {
            throw new NegativeValueException("Exceeded days of use cannot be negative");
        }

        if (exceededDaysOfUse == 0) {
            return 0;
        }

        return defaultRentPrice * exceededDaysOfUse;
    }

    public Integer calculateRentPrice(final List<ImmutablePair<Film, FilmRentRequest>> filmPairs) {
        return filmPairs.stream()
                .map(FilmRentCalculator::calculateSingleFilmRent)
                .reduce(Integer::sum)
                .orElseThrow(() -> new RentCalculationException("An error occurred during price calculation"));
    }

    public Integer calculateReturnPriceOld(List<ImmutablePair<Film, FilmReturnRequestOld>> filmPairs) {
        return filmPairs.stream()
                .map(FilmRentCalculator::calculateSingleFilmReturnPriceOld)
                .reduce(Integer::sum)
                .orElseThrow(() -> new RentCalculationException("An error occurred during price calculation"));
    }

    public Integer calculateReturnPrice(List<ImmutablePair<Film, FilmReturnSummary>> filmPairs) {
        return filmPairs.stream()
                .map(FilmRentCalculator::calculateSingleFilmReturnPrice)
                .reduce(Integer::sum)
                .orElseThrow(() -> new RentCalculationException("An error occurred during price calculation"));
    }

    private static Integer calculateSingleFilmRent(ImmutablePair<Film, FilmRentRequest> pair) {
        var type = pair.left.type();
        var includedDays = type.getIncludedDaysOfRent();
        var requestedDays = pair.right.amountOfDays();
        var defaultRentPrice = type.getPriceType().getPrice();

        if (requestedDays < 0) {
            throw new NegativeValueException("Requested days of use cannot be negative");
        }

        if (requestedDays == 0) {
            return 0;
        }

        return switch (type) {
            case NEW -> defaultRentPrice * requestedDays;
            case REGULAR, OLD -> {
                if (requestedDays <= includedDays) {
                    yield defaultRentPrice;
                } else {
                    yield defaultRentPrice + ((requestedDays - includedDays) * defaultRentPrice);
                }
            }
        };
    }
}
