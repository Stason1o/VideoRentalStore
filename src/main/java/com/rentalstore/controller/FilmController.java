package com.rentalstore.controller;

import com.rentalstore.dto.request.FilmRequest;
import com.rentalstore.dto.response.FilmResponse;
import com.rentalstore.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<FilmResponse> getInventory() {
        return filmService.getInventory();
    }

    @PostMapping
    public FilmResponse addFilmToInventory(@RequestBody FilmRequest request) {
        return filmService.addFilm(request);
    }
}
