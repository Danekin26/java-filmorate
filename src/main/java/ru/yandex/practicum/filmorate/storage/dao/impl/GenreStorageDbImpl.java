package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Component("databaseGenreStorage")
public class GenreStorageDbImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int idGenre) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE id_genre = ?", idGenre);
        if (sqlRowSet.next()) {
            return Genre.builder()
                    .id(sqlRowSet.getInt("id_genre"))
                    .name(sqlRowSet.getString("name_genre"))
                    .build();
        }
        throw new NotFoundException("Жанр с id = " + idGenre + " не существует");
    }

    @Override
    public List<Genre> getAllGenre() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM genre");
        List<Genre> allGenre = new ArrayList<>();
        while (sqlRowSet.next()) {
            Genre genre = Genre.builder()
                    .id(sqlRowSet.getInt("id_genre"))
                    .name(sqlRowSet.getString("name_genre"))
                    .build();
            allGenre.add(genre);
        }
        return allGenre;
    }

    @Override
    public Genre addGenre(String nameGenre) {
        jdbcTemplate.update("INSERT INTO genre (name_genre) VALUES ('?')", nameGenre);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT id_genre FROM genre WHERE name_genre LIKE '%?%'", nameGenre);
        return getGenreById(sqlRowSet.getInt("name_genre"));
    }

    @Override
    public void removeGenre(int idGenre) {
        jdbcTemplate.update("DELETE FROM genre WHERE id_genre = ?", idGenre);
    }
}
