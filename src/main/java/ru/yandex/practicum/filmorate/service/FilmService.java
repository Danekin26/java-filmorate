package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    /*
        Поставить лайк
     */
    public Film putLike(int idFilm, int idUser) {
        return filmStorage.putLikeFilm(idFilm, idUser);
    }

    /*
        Удалить лайк
     */
    public Film removeLike(int idFilm, int idUser) {
        return filmStorage.removeLikeFilm(idFilm, idUser);
    }

    /*
        Вывести список рейтинговых фильмов
     */
    public List<Film> getPopularFilms(int countFilms) {
        return filmStorage.getPopularFilms(countFilms);
    }
}
