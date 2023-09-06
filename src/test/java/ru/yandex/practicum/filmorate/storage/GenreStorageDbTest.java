package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.impl.GenreStorageDbImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageDbTest {

    @Autowired
    private GenreStorageDbImpl genreStorage;

    @Test
    public void getGenreByIdTest() {
        Genre genre1 = genreStorage.getGenreById(1);
        assertEquals(new Genre("Комедия", 1), genre1);

        Genre genre6 = genreStorage.getGenreById(6);
        assertEquals(new Genre("Боевик", 6), genre6);
    }

    @Test
    public void getAllGenreTest() {
        List<Genre> getAllGenre = genreStorage.getAllGenre();

        List<Genre> allGenre = new ArrayList<>();
        Genre genre1 = new Genre("Комедия", 1);
        Genre genre2 = new Genre("Драма", 2);
        Genre genre3 = new Genre("Мультфильм", 3);
        Genre genre4 = new Genre("Триллер", 4);
        Genre genre5 = new Genre("Документальный", 5);
        Genre genre6 = new Genre("Боевик", 6);

        allGenre.add(genre1);
        allGenre.add(genre2);
        allGenre.add(genre3);
        allGenre.add(genre4);
        allGenre.add(genre5);
        allGenre.add(genre6);

        assertEquals(allGenre, getAllGenre);
    }
}