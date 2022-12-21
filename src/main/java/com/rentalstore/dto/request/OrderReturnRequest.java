package com.rentalstore.dto.request;

import java.util.List;

public record OrderReturnRequest(Integer orderId, List<FilmReturnSummary> films) {
}
