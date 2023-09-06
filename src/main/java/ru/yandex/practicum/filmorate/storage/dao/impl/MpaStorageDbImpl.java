package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaStorage;

import java.util.ArrayList;
import java.util.List;

@Component("databaseMpaStorage")
public class MpaStorageDbImpl implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int idMpa) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id_mpa = ?", idMpa);
        if (sqlRowSet.next()) {
            return Mpa.builder()
                    .id(sqlRowSet.getInt("id_mpa"))
                    .name(sqlRowSet.getString("name_mpa"))
                    .build();
        }
        throw new NotFoundException("MPA с id = " + idMpa + " не найден");
    }

    @Override
    public List<Mpa> getAllMpa() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM mpa");
        List<Mpa> allMpa = new ArrayList<>();
        while (sqlRowSet.next()) {
            Mpa mpa = Mpa.builder()
                    .id(sqlRowSet.getInt("id_mpa"))
                    .name(sqlRowSet.getString("name_mpa"))
                    .build();
            allMpa.add(mpa);
        }
        return allMpa;
    }
}
