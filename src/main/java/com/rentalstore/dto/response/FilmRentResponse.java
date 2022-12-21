package com.rentalstore.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FilmRentResponse(Integer orderId, Integer priceForRent, List<String> films) {
}
