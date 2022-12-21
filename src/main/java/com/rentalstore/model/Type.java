package com.rentalstore.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.rentalstore.model.Price.BASIC;
import static com.rentalstore.model.Price.PREMIUM;

@RequiredArgsConstructor
@Getter
public enum Type {
    NEW(1, PREMIUM),
    REGULAR(3, BASIC),
    OLD(5, BASIC);

    private final Integer IncludedDaysOfRent;
    private final Price priceType;
}
