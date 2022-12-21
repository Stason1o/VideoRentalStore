package com.rentalstore.util.mappers;

import com.rentalstore.dto.request.FilmRequest;
import com.rentalstore.dto.response.FilmResponse;
import com.rentalstore.model.Film;
import org.springframework.stereotype.Service;

@Service
public class FilmMapper {

    public FilmResponse mapFilmToFilmResponse(final Film film) {
        return FilmResponse.builder()
                .name(film.name())
                .filmType(film.type().toString())
                .pricePerRent(film.type().getPriceType().getPrice())
                .baseRentDays(film.type().getIncludedDaysOfRent())
                .pricePerAdditionalRentDay(film.type().getPriceType().getPrice())
                .build();
    }

    public Film mapFilmRequestToFilm(final FilmRequest filmRequest) {
        return Film.builder()
                .name(filmRequest.name())
                .type(filmRequest.type()).build();
    }
}
