package com.rentalstore.util.mappers;

import com.rentalstore.dto.response.FilmRentResponse;
import com.rentalstore.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public FilmRentResponse mapOrderToFilmRentResponse(final Order order) {
        return FilmRentResponse.builder()
                .orderId(order.id())
                .priceForRent(order.price())
                .films(order.films())
                .build();
    }
}
