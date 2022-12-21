package com.rentalstore.repository.impl;

import com.rentalstore.exceptions.OrderNotFoundException;
import com.rentalstore.model.Film;
import com.rentalstore.model.Order;
import com.rentalstore.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rentalstore.repository.util.IdGenerator.generateId;
import static java.lang.String.format;

@Repository
@RequiredArgsConstructor
public class RentRepositoryImpl implements RentRepository {

    private static final List<Order> ORDERS = new ArrayList<>();

    @Override
    public Order getOrderById(Integer orderId) {
        return ORDERS.stream()
                .filter(order -> order.id().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(format("Order with id: %d does not exist", orderId)));
    }

    public Order rentFilms(final List<Film> films, final Integer price) {
        var filmNames = films.stream()
                .map(Film::name)
                .collect(Collectors.toList());

        var order = new Order(generateId(), filmNames, LocalDate.now(), price);

        ORDERS.add(order);

        return order;
    }

    public void returnFilms(Integer orderId) {
        ORDERS.removeIf(order -> order.id().equals(orderId));
    }

}
