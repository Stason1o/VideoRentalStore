package com.rentalstore.repository;

import com.rentalstore.model.Film;

import java.util.List;

public interface FilmRepository {

    List<Film> getInventory();

    List<Film> getFilms(final List<String> filmNames);

    Film addFilm(Film film);
}
