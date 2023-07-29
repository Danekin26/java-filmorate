package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.controller.UserController.validateUser;

class UserControllerTest {

    @Test
    public void userAdditionCheck() {
        User user = User.builder()
                .email("asdads@.com")
                .login("leq4")
                .name("Serge")
                .birthday(LocalDate.of(2000, 4, 12))
                .build();
        assertEquals("Валидация прошла успешно", validateUser(user));
    }

    @Test
    public void invalidEmail() {
        User user = User.builder()
                .email("asdads")
                .login("leq4")
                .name("Serge")
                .birthday(LocalDate.of(2000, 4, 12))
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> validateUser(user));
        assertEquals("email не может быть пустым и должен содержать знак @", ex.getMessage());
    }

    @Test
    public void invalidLogin() {
        User user = User.builder()
                .email("asdads@.com")
                .login("leq 4")
                .name("Serge")
                .birthday(LocalDate.of(2000, 4, 12))
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> validateUser(user));
        assertEquals("Логин не должен быть пустым и указывается без пробелов", ex.getMessage());
    }

    @Test
    public void creationUserWithoutName() {
        User user = User.builder()
                .email("asdads@.com")
                .login("leq4")
                .birthday(LocalDate.of(2000, 4, 12))
                .build();
        validateUser(user);
        assertEquals("leq4", user.getName());
    }

    @Test
    public void dateInTheFutureTense() {
        User user = User.builder()
                .email("asdads@.com")
                .login("leq4")
                .name("Serge")
                .birthday(LocalDate.of(2200, 4, 12))
                .build();
        ValidationException ex = assertThrows(ValidationException.class, () -> validateUser(user));
        assertEquals("Дата не может быть в будущем", ex.getMessage());
    }
}