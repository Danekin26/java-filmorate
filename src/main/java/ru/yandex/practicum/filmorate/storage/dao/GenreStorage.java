package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreById(int id);

    List<Genre> getAllGenre();

    Genre addGenre(String nameGenre);

    void removeGenre(int idGenre);
}
