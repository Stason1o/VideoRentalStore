package com.rentalstore.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RemainingOrder(Integer orderId, List<String> remainingFilmsToReturn) {
}
