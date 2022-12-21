package com.rentalstore.dto.response;

import lombok.Builder;

@Builder
public record FilmResponse(String name, String filmType, Integer pricePerRent, Integer baseRentDays,
                           Integer pricePerAdditionalRentDay) {
}
