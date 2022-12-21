package com.rentalstore.model;

import java.time.LocalDate;
import java.util.List;

public record Order(Integer id, List<String> films, LocalDate orderTime, Integer price) {
}
