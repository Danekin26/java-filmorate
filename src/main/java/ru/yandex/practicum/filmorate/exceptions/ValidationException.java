package ru.yandex.practicum.filmorate.exceptions;

/*
    Исключение при не пройденной валидации
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String mes) {
        super(mes);
    }
}
