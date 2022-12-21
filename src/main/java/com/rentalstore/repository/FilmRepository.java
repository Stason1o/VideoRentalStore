package com.rentalstore.repository;

import com.rentalstore.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    List<Film> getInventory();

    Optional<Film> getFilm(final String name);

    List<Film> getFilms(final List<String> filmNames);

    Film addFilm(Film film);
}
