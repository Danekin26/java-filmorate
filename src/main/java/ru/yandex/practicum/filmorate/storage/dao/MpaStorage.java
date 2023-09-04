package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    /*
        Получить рейтинг по id
     */
    Mpa getMpaById(int idMpa);

    /*
        Получить все виды рейтинга
     */
    List<Mpa> getAllMpa();
}
