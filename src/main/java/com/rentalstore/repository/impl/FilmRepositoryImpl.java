package com.rentalstore.repository.impl;

import com.rentalstore.model.Film;
import com.rentalstore.repository.FilmRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rentalstore.model.Type.*;

@Repository
public class FilmRepositoryImpl implements FilmRepository {

    private static final List<Film> INVENTORY = new ArrayList<>(26);

    static {
        INVENTORY.add(new Film("Spider Man 1", REGULAR));
        INVENTORY.add(new Film("Harry Potter 1", REGULAR));
        INVENTORY.add(new Film("Harry Potter 2", REGULAR));
        INVENTORY.add(new Film("Harry Potter 3", REGULAR));
        INVENTORY.add(new Film("Harry Potter 4", REGULAR));
        INVENTORY.add(new Film("Harry Potter 5", REGULAR));
        INVENTORY.add(new Film("Harry Potter 6", REGULAR));
        INVENTORY.add(new Film("Harry Potter 7 part 1", REGULAR));
        INVENTORY.add(new Film("Harry Potter 7 part 2", REGULAR));
        INVENTORY.add(new Film("Lord of the rings 1", OLD));
        INVENTORY.add(new Film("Lord of the rings 2", OLD));
        INVENTORY.add(new Film("Lord of the rings 3", OLD));
        INVENTORY.add(new Film("Hobbit 1", OLD));
        INVENTORY.add(new Film("Hobbit 2", OLD));
        INVENTORY.add(new Film("Hobbit 3", OLD));
        INVENTORY.add(new Film("Avatar 1", OLD));
        INVENTORY.add(new Film("Avatar 2", NEW));
        INVENTORY.add(new Film("Black Panther Wakanda Forever", NEW));
        INVENTORY.add(new Film("Matrix 4", NEW));
        INVENTORY.add(new Film("The Green Mile", OLD));
        INVENTORY.add(new Film("Forest Gump", OLD));
        INVENTORY.add(new Film("Interstellar", NEW));
        INVENTORY.add(new Film("Guardians of the Galaxy: Holiday Special", NEW));
        INVENTORY.add(new Film("Black Adam", NEW));
        INVENTORY.add(new Film("One Way", NEW));
        INVENTORY.add(new Film("Avatar: The Last Airbender", OLD));
    }

    public List<Film> getInventory() {
        return INVENTORY;
    }

    public Optional<Film> getFilm(final String name) {
        return INVENTORY.stream()
                .filter(film -> film.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Film> getFilms(final List<String> filmNames) {
        return filmNames.stream()
                .flatMap(name -> INVENTORY.stream()
                        .filter(film -> film.name().equalsIgnoreCase(name))).toList();
    }

    public Film addFilm(Film film) {
        INVENTORY.add(film);
        return film;
    }

}
