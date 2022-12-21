package com.rentalstore.service;

import com.rentalstore.dto.request.FilmRequest;
import com.rentalstore.dto.response.FilmResponse;
import com.rentalstore.repository.FilmRepository;
import com.rentalstore.util.mappers.FilmMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public List<FilmResponse> getInventory() {
        return filmRepository.getInventory()
                .stream()
                .map(filmMapper::mapFilmToFilmResponse)
                .toList();
    }

    public FilmResponse addFilm(FilmRequest request) {
        return filmMapper.mapFilmToFilmResponse(filmRepository.addFilm(filmMapper.mapFilmRequestToFilm(request)));
    }
}
