package com.rentalstore.model;

import lombok.Builder;

@Builder
public record Film(String name, Type type) implements Comparable<Film> {
    @Override
    public int compareTo(Film o) {
        return this.name.compareTo(o.name);
    }
}
