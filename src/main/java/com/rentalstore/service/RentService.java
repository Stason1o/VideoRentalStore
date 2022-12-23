package com.rentalstore.service;

import com.google.common.collect.Streams;
import com.rentalstore.dto.request.FilmRentRequest;
import com.rentalstore.dto.request.FilmReturnRequestOld;
import com.rentalstore.dto.request.FilmReturnSummary;
import com.rentalstore.dto.request.OrderReturnRequest;
import com.rentalstore.dto.response.FilmRentResponse;
import com.rentalstore.exceptions.IncompleteOrderException;
import com.rentalstore.repository.FilmRepository;
import com.rentalstore.repository.RentRepository;
import com.rentalstore.service.calculator.FilmRentCalculator;
import com.rentalstore.util.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentService {

    private final RentRepository rentRepository;
    private final FilmRepository filmRepository;
    private final FilmRentCalculator filmRentCalculator;
    private final OrderMapper orderMapper;

    public Integer calculateRentPrice(List<FilmRentRequest> filmsForRent) {
        var filmNames = filmsForRent
                .stream()
                .map(FilmRentRequest::name)
                .toList();
        var existingFilms = filmRepository.getFilms(filmNames);
        var filmPairs = Streams.zip(existingFilms.stream(), filmsForRent.stream(), ImmutablePair::new)
                .toList();

        return filmRentCalculator.calculateRentPrice(filmPairs);
    }

    public FilmRentResponse rentFilms(List<FilmRentRequest> filmsForRent) {
        var filmNames = filmsForRent
                .stream()
                .map(FilmRentRequest::name)
                .toList();
        var existingFilms = filmRepository.getFilms(filmNames);
        var filmPairs = Streams.zip(existingFilms.stream(), filmsForRent.stream(), ImmutablePair::new)
                .toList();

        var rentPrice = filmRentCalculator.calculateRentPrice(filmPairs);
        var order = rentRepository.rentFilms(existingFilms, rentPrice);

        return orderMapper.mapOrderToFilmRentResponse(order);
    }

    public Integer returnFilmsOld(List<FilmReturnRequestOld> filmsToReturn) {
        var filmNames = filmsToReturn
                .stream()
                .map(FilmReturnRequestOld::name)
                .toList();

        var existingFilms = filmRepository.getFilms(filmNames);

        var filmPairs = Streams.zip(existingFilms.stream(), filmsToReturn.stream(), ImmutablePair::new)
                .toList();
        return filmRentCalculator.calculateReturnPriceOld(filmPairs);
    }

    public Integer returnFilms(OrderReturnRequest filmsToReturn) {
        checkIfReturningOrderIsComplete(filmsToReturn);

        var filmNames = filmsToReturn.films()
                .stream()
                .map(FilmReturnSummary::name)
                .toList();

        var existingFilms = filmRepository.getFilms(filmNames);

        var filmPairs = Streams.zip(existingFilms.stream(), filmsToReturn.films().stream(), ImmutablePair::new)
                .toList();

        var priceForExceededReturnTime = filmRentCalculator.calculateReturnPrice(filmPairs);

        rentRepository.returnFilms(filmsToReturn.orderId());

        return priceForExceededReturnTime;
    }

    private void checkIfReturningOrderIsComplete(OrderReturnRequest orderReturnRequest) {
        var existingOrder = rentRepository.getOrderById(orderReturnRequest.orderId());

        boolean isValidOrder = existingOrder.films().size() == orderReturnRequest.films().size() &&
                new HashSet<>(existingOrder.films())
                        .containsAll(orderReturnRequest.films().stream().map(FilmReturnSummary::name).toList());

        if (!isValidOrder) {
            throw new IncompleteOrderException(String.format("Order with id: '%d' is not complete", orderReturnRequest.orderId()));
        }
    }
}
