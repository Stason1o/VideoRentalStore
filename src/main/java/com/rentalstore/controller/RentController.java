package com.rentalstore.controller;

import com.rentalstore.dto.request.FilmRentRequest;
import com.rentalstore.dto.request.FilmReturnRequestOld;
import com.rentalstore.dto.request.OrderReturnRequest;
import com.rentalstore.dto.response.FilmRentResponse;
import com.rentalstore.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/rent")
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;

    @PostMapping("/price")
    public Integer getRentPrice(@RequestBody List<FilmRentRequest> filmsForRent) {
        return rentService.calculateRentPrice(filmsForRent);
    }

    @PostMapping
    public FilmRentResponse rentFilms(@RequestBody List<FilmRentRequest> filmsForRent) {
        return rentService.rentFilms(filmsForRent);
    }

    @PostMapping("/return")
    public Integer returnFilms(@RequestBody OrderReturnRequest filmReturnForm) {
        return rentService.returnFilms(filmReturnForm);
    }

    @PostMapping("/return/old")
    public Integer returnFilmsOld(@RequestBody List<FilmReturnRequestOld> filmReturnForm) {
        return rentService.returnFilmsOld(filmReturnForm);
    }
}
