package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

/*
    Контроллер добавления/обновления/получения фильмов
 */
@RestController
@Slf4j
@Component
public class FilmController {
    @Autowired
    private FilmService filmService;

    /*
        Добавить фильм
     */
    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        log.debug("Выполнен POST-запрос");
        return filmService.addFilm(film);
    }

    /*
        Обновить фильм
     */
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Выполнен PUT-запрос");
        return filmService.updateFilm(film);
    }

    /*
        Получить все фильмы
     */
    @GetMapping("/films")
    public List<Film> getFilm() {
        log.debug("Выполнен GET-запрос");
        return filmService.getAllFilm();
    }

    /*
        Пользователь ставит лайк фильму
     */
    @PutMapping("/films/{id}/like/{userId}")
    public Film putLikeFilm(@PathVariable String id, @PathVariable String userId) {
        return filmService.putLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    /*
        Пользователя удаляет лайк
     */
    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable String id, @PathVariable String userId) {
        return filmService.removeLike(Integer.parseInt(id), Integer.parseInt(userId));
    }

    /*
        Получить рейтинговые фильмы
     */
    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getPopularFilms(Integer.parseInt(count));
    }

    /*
        Получить фильм по id
     */
    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable String id) {
        return filmService.getFilmById(Integer.parseInt(id));
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable Integer id) {
        filmService.deleteFilm(id);
    }
}
