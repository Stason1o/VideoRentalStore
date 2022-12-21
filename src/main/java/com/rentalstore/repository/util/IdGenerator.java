package com.rentalstore.repository.util;

import com.rentalstore.exceptions.IdGenerationException;

import java.util.Random;

public class IdGenerator {

    private static final Random RANDOM = new Random();

    public static int generateId() {
        return RANDOM.ints(0, Integer.MAX_VALUE)
                .findFirst()
                .orElseThrow(() -> new IdGenerationException("Error generating ID for order"));
    }
}
