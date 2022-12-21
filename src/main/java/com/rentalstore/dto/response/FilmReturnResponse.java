package com.rentalstore.dto.response;

import com.rentalstore.dto.RemainingOrder;

import java.util.List;

public record FilmReturnResponse(Integer priceForReturnedItems, List<RemainingOrder> remainingFilmsToReturn) {
}
