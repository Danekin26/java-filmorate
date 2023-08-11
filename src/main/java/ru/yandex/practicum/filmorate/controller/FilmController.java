package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

import static ru.yandex.practicum.filmorate.validation.QueryValidation.validateFilm;

/*
    Контроллер добавления/обновления/получения фильмов
 */
@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> idAndFilm = new HashMap<>();
    private int nextId = 1;

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        log.debug("Выполнен POST-запрос");
        validateFilm(film);
        film.setId(nextId);
        idAndFilm.put(nextId, film);
        nextId++;
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Выполнен PUT-запрос");
        validateFilm(film);
        int idFilm = film.getId();
        if ((idAndFilm.containsKey(idFilm)) && idFilm != 0) {
            idAndFilm.put(idFilm, film);
            return film;
        } else {
            throw new ValidationException("Фильма с таким id не существует.");
        }
    }

    @GetMapping("/films")
    public ArrayList<Film> getFilm() {
        log.debug("Выполнен GET-запрос");
        return new ArrayList<>(idAndFilm.values());
    }
}
