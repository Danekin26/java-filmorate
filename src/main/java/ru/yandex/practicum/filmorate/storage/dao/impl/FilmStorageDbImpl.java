package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validation.QueryValidation.validateFilm;

@Component("databaseFilmStorage")
public class FilmStorageDbImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        jdbcTemplate.update("INSERT INTO film (name_film , description, release_date, duration, rating_mpa) "
                        + "VALUES(?,?,?,?,?)",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE name_film = ? ", film.getName());
        if (sqlRowSet.next()) {
            film.setId(sqlRowSet.getInt("id_film"));
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        } else {
            addLinkBetweenMoviesAndGenres(film.getId(), film.getGenres());
            film.setGenres(getFilmGenres(film.getId()));
        }
        film.setMpa(getMpaById(film.getMpa().getId()));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        film.setMpa(getMpaById(film.getMpa().getId()));
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        jdbcTemplate.update("DELETE FROM film_genre WHERE id_film = ?", film.getId());
        if (film.getGenres() == null || film.getGenres().size() == 0) {
            film.setGenres(new ArrayList<>());
        } else {
            addLinkBetweenMoviesAndGenres(film.getId(), film.getGenres());
            film.setGenres(getFilmGenres(film.getId()));
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM film where id_film = ?", film.getId());
        if (sqlRowSet.next()) {
            jdbcTemplate.update("UPDATE film SET name_film = ?, description = ?, release_date = ?, duration = ?, rating_mpa = ? WHERE id_film = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    //film.getRate(),
                    film.getId());
            return film;
        }
        throw new NotFoundException("Фильма с id = " + film.getId() + " не существует");
    }

    @Override
    public List<Film> getAllFilms() {
        SqlRowSet filmsRow = jdbcTemplate.queryForRowSet("SELECT * FROM film");
        List<Film> allFilms = new ArrayList<>();
        while (filmsRow.next()) {
            allFilms.add(convertRequestToFilm(filmsRow.getInt("id_film")));
        }
        return allFilms;
    }

    @Override
    public Film putLikeFilm(int idFilm, int idUser) {
        Film film = getFilmById(idFilm);
        validateUser(idUser);
        film.addLikeForSet(idUser);
        jdbcTemplate.update("INSERT INTO like_film (id_film, id_users) VALUES (?, ?)", idFilm, idUser);
        return film;
    }

    @Override
    public Film removeLikeFilm(int idFilm, int idUser) {
        Film film = getFilmById(idFilm);
        validateUser(idUser);
        film.removeLikeForSet(idUser);
        jdbcTemplate.update("DELETE FROM like_film WHERE id_film = ?", idFilm);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int countFilms) {
        SqlRowSet filmsRow = jdbcTemplate.queryForRowSet("SELECT f.id_film FROM film AS f LEFT OUTER JOIN like_film AS lf ON f.id_film = lf.id_film GROUP BY f.id_film ORDER BY COUNT(lf.id_users) DESC LIMIT ?", countFilms);
        List<Film> popularFilms = new ArrayList<>();
        while (filmsRow.next()) {
            popularFilms.add(convertRequestToFilm(filmsRow.getInt("id_film")));
        }
        return popularFilms;
    }

    @Override
    public Film getFilmById(int idFilm) {
        SqlRowSet filmsRow = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE id_film = ?", idFilm);
        if (!filmsRow.next()) {
            throw new NotFoundException("Фильм не найден в базе данных");
        }
        return convertRequestToFilm(idFilm);
    }

    @Override
    public void deleteFilm(Integer id) {
        jdbcTemplate.update("DELETE FROM film WHERE id_film = ?", id);
        jdbcTemplate.update("DELETE FROM film_genre WHERE id_film = ?", id);
        jdbcTemplate.update("DELETE FROM like_film WHERE id_film = ?", id);
    }

    private List<Genre> getFilmGenres(int idFilm) {
        SqlRowSet newRowSet = jdbcTemplate.queryForRowSet("SELECT fg.id_film, g.name_genre, g.id_genre FROM film_genre AS fg INNER JOIN genre g ON fg.id_genre = g.id_genre");
        List<Genre> allGenreForFilm = new ArrayList<>();
        while (newRowSet.next()) {
            if (newRowSet.getInt("id_film") == idFilm) {
                allGenreForFilm.add(new Genre(newRowSet.getString("name_genre"), newRowSet.getInt("id_genre")));
            }
        }
        return allGenreForFilm;
    }

    private void addLinkBetweenMoviesAndGenres(int idFilm, List<Genre> allGenre) {
        if (allGenre != null && !allGenre.isEmpty()) {
            for (Genre genre : allGenre) {
                if (!checkRepeatGenreForFilm(idFilm, genre.getId())) {
                    jdbcTemplate.update("INSERT INTO film_genre (id_film, id_genre) VALUES (?, ?)", idFilm, genre.getId());
                }
            }
        }
    }

    private boolean checkRepeatGenreForFilm(int idFilm, int idGenre) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM film_genre WHERE id_film = ? AND id_genre = ?", idFilm, idGenre);
        if (sqlRowSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    private Set<Integer> getLikesForFilm(int idFilm) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT lf.* FROM like_film AS lf WHERE lf.id_film = ?", idFilm);
        Set<Integer> likeFilm = new HashSet<>();
        while (rowSet.next()) {
            likeFilm.add(rowSet.getInt("id_users"));
        }
        return likeFilm;
    }

    private Film convertRequestToFilm(int idFilm) {
        SqlRowSet ratingFilmRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE id_film = ?", idFilm);
        if (ratingFilmRowSet.next()) {
            return Film.builder()
                    .id(idFilm)
                    .name(ratingFilmRowSet.getString("name_film"))
                    .description(ratingFilmRowSet.getString("description"))
                    .releaseDate(ratingFilmRowSet.getDate("release_date").toLocalDate())
                    .duration(ratingFilmRowSet.getInt("duration"))
                    .mpa(getMpaById(ratingFilmRowSet.getInt("rating_mpa")))
                    .genres(getFilmGenres(idFilm))
                    .likes(getLikesForFilm(idFilm))
                    .build();
        }
        throw new NotFoundException("Пользователя с id " + idFilm + " не найден");
    }

    private Mpa getMpaById(int idMpa) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id_mpa = ?", idMpa);
        if (sqlRowSet.next()) {
            return Mpa.builder()
                    .id(sqlRowSet.getInt("id_mpa"))
                    .name(sqlRowSet.getString("name_mpa"))
                    .build();
        }
        throw new NotFoundException("MPA c id = " + idMpa + " не найдена");
    }

    private void validateUser(int idUser) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_users = ?", idUser);
        if (!sqlRowSet.next()) {
            throw new NotFoundException("Пользователя с id = " + idUser + " не существует");
        }
    }
}
