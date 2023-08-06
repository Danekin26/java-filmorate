package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

/*
    Утилитарный класс, хранящий валидацию пользователей и фильмов
 */
@Slf4j
public class QueryValidation {
    public static void validateUser(User user) {
        if (user.getEmail().isBlank() || user.getEmail().indexOf('@') < 0) {
            log.debug("Ошибка с email");
            throw new ValidationException("email не может быть пустым и должен содержать знак @");
        }
        if (user.getLogin().isBlank() || user.getLogin().indexOf(' ') > 0) {
            log.debug("Ошибка с логином");
            throw new ValidationException("Логин не должен быть пустым и указывается без пробелов");
        }
        if ((user.getName() == null) || (user.getName().isBlank())) {
            log.debug("Имени присваивается логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Ошибка с датой");
            throw new ValidationException("Дата не может быть в будущем");
        }
        log.debug("Валидация прошла успешно");
    }

    public static void validateFilm(Film film) {
        if (film.getName().isBlank()) {
            log.debug("Ошибка с названием фильма");
            throw new ValidationException("Пустое название фильма");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Ошибка с описанием");
            throw new ValidationException("Длинное описание");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Ошибка с датой релиза");
            throw new ValidationException("Дата релиза должна быть не раньше 28.12.1895");
        }
        if (film.getDuration() < 0) {
            log.debug("Ошибка с длиною фильма");
            throw new ValidationException("Продолжительность фильма не может быть отрицательным");
        }
    }
}
