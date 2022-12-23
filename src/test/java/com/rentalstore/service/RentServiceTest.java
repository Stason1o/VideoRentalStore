package com.rentalstore.service;

import com.rentalstore.dto.request.FilmRentRequest;
import com.rentalstore.dto.request.FilmReturnRequestOld;
import com.rentalstore.dto.request.FilmReturnSummary;
import com.rentalstore.dto.request.OrderReturnRequest;
import com.rentalstore.dto.response.FilmRentResponse;
import com.rentalstore.exceptions.IncompleteOrderException;
import com.rentalstore.exceptions.OrderNotFoundException;
import com.rentalstore.model.Film;
import com.rentalstore.model.Order;
import com.rentalstore.model.Type;
import com.rentalstore.repository.FilmRepository;
import com.rentalstore.repository.RentRepository;
import com.rentalstore.service.calculator.FilmRentCalculator;
import com.rentalstore.util.mappers.OrderMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    private final FilmRentCalculator filmRentCalculator = new FilmRentCalculator();

    @Mock
    private RentRepository rentRepository;
    @Mock
    private FilmRepository filmRepository;
    @Mock
    private OrderMapper orderMapper;

    private RentService rentService;

    @BeforeEach
    void setUp() {
        rentService = new RentService(rentRepository, filmRepository, filmRentCalculator, orderMapper);
    }

    @Test
    void shouldCalculateRentPrice() {
        var request = asList(new FilmRentRequest("Film 1", 10),
                new FilmRentRequest("Film 2", 3));

        when(filmRepository.getFilms(asList("Film 1", "Film 2")))
                .thenReturn(
                        asList(Film.builder()
                                        .type(Type.REGULAR)
                                        .name("Film 1")
                                        .build(),
                                Film.builder()
                                        .type(Type.NEW)
                                        .name("Film 2")
                                        .build()
                        ));

        var price = rentService.calculateRentPrice(request);

        Assertions.assertThat(price).isEqualTo(360);
    }

    @Test
    void shouldRentFilms() {
        var request = asList(new FilmRentRequest("Film 1", 10),
                new FilmRentRequest("Film 2", 3));

        var existingFilms = asList(Film.builder()
                        .type(Type.REGULAR)
                        .name("Film 1")
                        .build(),
                Film.builder()
                        .type(Type.NEW)
                        .name("Film 2")
                        .build()
        );
        var filmNames = asList("Film 1", "Film 2");
        var orderId = 1;
        var priceForRent = 360;
        var order = new Order(orderId, filmNames, LocalDate.now(), priceForRent);

        when(filmRepository.getFilms(filmNames))
                .thenReturn(existingFilms);
        when(rentRepository.rentFilms(existingFilms, priceForRent))
                .thenReturn(order);
        var expectedResponse = new FilmRentResponse(orderId, priceForRent, filmNames);

        when(orderMapper.mapOrderToFilmRentResponse(order))
                .thenReturn(expectedResponse);

        var response = rentService.rentFilms(request);


        Assertions.assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnFilms() {
        var orderId = 1;
        var priceForExceededReturnTime = 270;
        var filmReturnSummaries = asList(new FilmReturnSummary("Film 1", 5),
                new FilmReturnSummary("Film 2", 3));
        var request = new OrderReturnRequest(orderId, filmReturnSummaries);

        var existingFilms = asList(Film.builder()
                        .type(Type.REGULAR)
                        .name("Film 1")
                        .build(),
                Film.builder()
                        .type(Type.NEW)
                        .name("Film 2")
                        .build()
        );
        var filmNames = asList("Film 1", "Film 2");
        var order = new Order(orderId, filmNames, LocalDate.now(), 210);

        when(filmRepository.getFilms(filmNames))
                .thenReturn(existingFilms);
        when(rentRepository.getOrderById(orderId))
                .thenReturn(order);
        doNothing()
                .when(rentRepository)
                .returnFilms(orderId);

        var response = rentService.returnFilms(request);

        Assertions.assertThat(response).isEqualTo(priceForExceededReturnTime);
    }

    @Test
    void shouldReturnFilmsOld() {
        var priceForExceededReturnTime = 270;
        var request = asList(new FilmReturnRequestOld("Film 1", 5),
                new FilmReturnRequestOld("Film 2", 3));

        var existingFilms = asList(Film.builder()
                        .type(Type.REGULAR)
                        .name("Film 1")
                        .build(),
                Film.builder()
                        .type(Type.NEW)
                        .name("Film 2")
                        .build()
        );
        var filmNames = asList("Film 1", "Film 2");

        when(filmRepository.getFilms(filmNames))
                .thenReturn(existingFilms);

        var response = rentService.returnFilmsOld(request);

        Assertions.assertThat(response).isEqualTo(priceForExceededReturnTime);
    }

    @Test
    void shouldThrowExceptionWhenIncompleteOrder() {
        var orderId = 1;
        var filmReturnSummaries = singletonList(new FilmReturnSummary("Film 1", 5));
        var request = new OrderReturnRequest(orderId, filmReturnSummaries);
        var filmNames = asList("Film 1", "Film 2");
        var order = new Order(orderId, filmNames, LocalDate.now(), 210);

        when(rentRepository.getOrderById(orderId))
                .thenReturn(order);

        Assertions.assertThatExceptionOfType(IncompleteOrderException.class)
                .isThrownBy(() -> rentService.returnFilms(request));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        var orderId = 1;
        var filmReturnSummaries = singletonList(new FilmReturnSummary("Film 1", 5));
        var request = new OrderReturnRequest(orderId, filmReturnSummaries);

        doThrow(OrderNotFoundException.class)
                .when(rentRepository)
                .getOrderById(orderId);

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> rentService.returnFilms(request));
    }
}