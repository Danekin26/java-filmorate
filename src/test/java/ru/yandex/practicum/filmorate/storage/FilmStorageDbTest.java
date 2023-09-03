package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.impl.FilmStorageDbImpl;
import ru.yandex.practicum.filmorate.storage.dao.impl.UserStorageDbImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageDbTest {
    @Autowired
    private FilmStorageDbImpl filmStorage;
    @Autowired
    private UserStorageDbImpl userStorage;

    @Test
    public void getFilmByIdTest() {
        Film film = Film.builder()
                .id(1)
                .name("Тестовый фильм")
                .description("Описание тестового фильма")
                .releaseDate(LocalDate.parse("2022-12-21"))
                .duration(1000)
                .mpa(new Mpa("G", 1))
                .build();
        filmStorage.addFilm(film);

        Film filmGetById = filmStorage.getFilmById(film.getId());

        assertEquals(film, filmGetById);
        filmStorage.deleteFilm(film.getId());
    }

    @Test
    public void updateFilmTest() {
        Film film = Film.builder()
                .id(1)
                .name("Тестовый фильм")
                .description("Описание тестового фильма")
                .releaseDate(LocalDate.parse("2022-12-21"))
                .duration(1000)
                .mpa(new Mpa("G", 1))
                .build();
        filmStorage.addFilm(film);

        Film update = Film.builder()
                .id(1)
                .name("Обновленный тестовый фильм")
                .description("Описание обновленного тестового фильма")
                .releaseDate(LocalDate.parse("2002-12-21"))
                .duration(3000)
                .mpa(new Mpa("PG", 4))
                .build();
        update.setId(film.getId());

        Film updated = filmStorage.updateFilm(update);

        assertEquals(update, updated);
        filmStorage.deleteFilm(film.getId());
    }

    @Test
    public void getPopularFilmsTest() {
        Film film1 = Film.builder()
                .id(1)
                .name("Тестовый фильм 1")
                .description("Описание тестового фильма 1")
                .releaseDate(LocalDate.parse("2022-12-21"))
                .duration(1000)
                .mpa(new Mpa("G", 1))
                .build();
        filmStorage.addFilm(film1);
        Film film2 = Film.builder()
                .id(2)
                .name("Тестовый фильм 2")
                .description("Описание тестового фильма 2")
                .releaseDate(LocalDate.parse("2022-12-21"))
                .duration(1000)
                .mpa(new Mpa("G", 1))
                .build();
        filmStorage.addFilm(film2);
        User user = User.builder()
                .id(1)
                .email("testMail@java.com")
                .login("Cats")
                .name("Alex")
                .birthday(LocalDate.parse("1990-01-12"))
                .build();
        userStorage.addUser(user);
        filmStorage.putLikeFilm(film1.getId(), user.getId());
        film1 = filmStorage.getFilmById(1);
        List<Film> popularFilms = filmStorage.getPopularFilms(2);
        assertEquals(film1, popularFilms.get(0));
        assertEquals(film2, popularFilms.get(1));
        filmStorage.deleteFilm(film1.getId());
        filmStorage.deleteFilm(film2.getId());
    }
}
