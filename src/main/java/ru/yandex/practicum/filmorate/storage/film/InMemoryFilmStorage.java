package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validation.QueryValidation.validateFilm;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> idAndFilm = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(nextId);
        idAndFilm.put(nextId, film);
        nextId++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        int idFilm = film.getId();
        if ((idAndFilm.containsKey(idFilm)) && idFilm != 0) {
            idAndFilm.put(idFilm, film);
            return film;
        } else {
            throw new ValidationException("Фильма с таким id не существует.");
        }
    }

    @Override
    public ArrayList<Film> getAllFilm() {
        return new ArrayList<>(idAndFilm.values());
    }

    @Override
    public Film putLikeFilm(int idFilm, int idUser) {
        validateFilm(idAndFilm.get(idFilm));
        if (idUser < 0) throw new NotFoundException("id не может быть отрицательным");
        idAndFilm.get(idFilm).addLikeForSet(idUser);
        return idAndFilm.get(idFilm);
    }

    @Override
    public Film removeLikeFilm(int idFilm, int idUser) {
        validateFilm(idAndFilm.get(idFilm));
        if (idUser < 0) throw new NotFoundException("id не может быть отрицательным");
        idAndFilm.get(idFilm).removeLikeForSet(idUser);
        return idAndFilm.get(idFilm);
    }

    @Override
    public List<Film> getPopularFilms(int countFilms) {
        if (countFilms < 0) throw new NotFoundException("Размер списка рейтинговых фильмов не может быть меньше 0");
        return getAllFilm().stream()
                .sorted((film1, film2) -> film2.getListLikes() - film1.getListLikes())
                .limit(countFilms)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(int idFilm) {
        validateFilm(idAndFilm.get(idFilm));
        return idAndFilm.get(idFilm);
    }
}
