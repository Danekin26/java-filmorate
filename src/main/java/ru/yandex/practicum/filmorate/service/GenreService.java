package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    @Qualifier("databaseGenreStorage")
    private GenreStorage genreStorage;

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }

    public Genre addGenre(String nameGenre) {
        return genreStorage.addGenre(nameGenre);
    }

    public void removeGenre(int idGenre) {
        genreStorage.removeGenre(idGenre);
    }
}
