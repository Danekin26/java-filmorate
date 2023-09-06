package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    /*
        Получить жанр по id
     */
    Genre getGenreById(int id);

    /*
        Получить все виды жанров
     */
    List<Genre> getAllGenre();

    /*
        Добавить жанр
     */
    Genre addGenre(String nameGenre);

    /*
        Удалить жанр по id
     */
    void removeGenre(int idGenre);
}
