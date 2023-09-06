package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    /*
        Добавить фильм
     */
    Film addFilm(Film film);

    /*
        Обновить информацию о фильме
     */
    Film updateFilm(Film film);

    /*
        Получить список всех фильмов
     */
    List<Film> getAllFilms();

    /*
        Поставить лайк фильму
     */
    Film putLikeFilm(int idFilm, int idUser);

    /*
        Удалить лайк у фильма
     */
    Film removeLikeFilm(int idFilm, int idUser);

    /*
        Вывод фильмов с наибольшим количеством лайков
     */
    List<Film> getPopularFilms(int countFilms);

    /*
        Получить фильм по id
     */
    Film getFilmById(int idFilm);

    /*
    Удалить фильм по id
    */
    void deleteFilm(Integer id);
}