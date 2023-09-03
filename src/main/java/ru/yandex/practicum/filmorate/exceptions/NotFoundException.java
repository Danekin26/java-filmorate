package ru.yandex.practicum.filmorate.exceptions;

/*
    Исключение при отсутствии запрашиваемого объекта
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String mes) {
        super(mes);
    }
}
