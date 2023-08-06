package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.validation.QueryValidation.validateFilm;

class FilmControllerTest {

    @Test
    public void createFilm() {
        Film film = Film.builder()
                .name("Film1")
                .description("description")
                .releaseDate(LocalDate.of(2015, 7, 21))
                .duration(70)
                .build();

        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmStorage, filmService);
        filmController.addFilm(film);
        assertEquals(film, filmController.getFilm().get(0));
    }

    @Test
    public void createFilmWithEmptyName() {
        Film film = Film.builder()
                .name(" ")
                .description("description")
                .releaseDate(LocalDate.of(2015, 7, 21))
                .duration(70)
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> validateFilm(film));
        assertEquals("Пустое название фильма", ex.getMessage());
    }

    @Test
    public void createFilmWithLongDescription() {
        Film film = Film.builder()
                .name("Film1")
                .description("description description description description description description description"
                        + " description description description description description description description"
                        + " description description description description description description description"
                        + " description description description description description description description"
                        + " description description description description description description description"
                        + " description description description description description description description")
                .releaseDate(LocalDate.of(2015, 7, 21))
                .duration(70)
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> validateFilm(film));
        assertEquals("Длинное описание", ex.getMessage());
    }

    @Test
    public void createFilmWithEarlyDate() {
        Film film = Film.builder()
                .name("Film1")
                .description("description")
                .releaseDate(LocalDate.of(1200, 7, 21))
                .duration(70)
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> validateFilm(film));
        assertEquals("Дата релиза должна быть не раньше 28.12.1895", ex.getMessage());
    }

    @Test
    public void createFilmWithNegativeDuration() {
        Film film = Film.builder()
                .name("Film1")
                .description("description")
                .releaseDate(LocalDate.of(2015, 7, 21))
                .duration(-70)
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> validateFilm(film));
        assertEquals("Продолжительность фильма не может быть отрицательным", ex.getMessage());
    }
}