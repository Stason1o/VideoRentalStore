package com.rentalstore.repository;

import com.rentalstore.model.Film;
import com.rentalstore.model.Order;

import java.util.List;

public interface RentRepository {

    Order rentFilms(final List<Film> films, final Integer price);

    void returnFilms(Integer orderId);

    Order getOrderById(Integer orderId);
}
