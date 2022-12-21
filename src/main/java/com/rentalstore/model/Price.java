package com.rentalstore.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Price {
    BASIC(30), PREMIUM(40);
    private final Integer price;
}
