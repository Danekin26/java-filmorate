package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

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

    public static String validateFilm(Film film) {
        if (film.getName().isBlank()) {
            log.debug("Ошибка с названием фильма");
            throw new ValidationException("Пустое название фильма");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Ошибка с описанием");
            throw new ValidationException("Длинное описание");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Ошибка с датой релиза");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895");
        }
        if (film.getDuration() < 0) {
            log.debug("Ошибка с длиною фильма");
            throw new ValidationException("Продолжительность фильма не может быть отрицательным");
        }
        return "Валидация прошла успешно";
    }
}
