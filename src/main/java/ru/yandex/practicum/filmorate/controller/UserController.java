package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    private final HashMap<Integer, User> idAndUser = new HashMap<>();
    private int nextId = 1;

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        log.debug("Выполнен POST-запрос");
        validateUser(user);
        idAndUser.put(nextId, user);
        user.setId(nextId);
        nextId++;
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.debug("Выполнен PUT-запрос");
        validateUser(user);
        int idUser = user.getId();
        if (idAndUser.containsKey(idUser)) {
            idAndUser.put(idUser, user);
            return user;
        } else {
            throw new ValidationException("Пользователя с таким id не существует.");
        }
    }

    @GetMapping("/users")
    public ArrayList<User> getUsers() {
        log.debug("Выполнен GET-запрос");
        return new ArrayList<>(idAndUser.values());
    }

    public static String validateUser(User user) {
        if (user.getEmail().isBlank() || user.getEmail().indexOf('@') < 0) {
            log.debug("Ошибка с email");
            throw new ValidationException("email не может быть пустым и должен содержать знак @");
        }
        if (user.getLogin().isBlank() || user.getLogin().indexOf(' ') > 0) {
            log.debug("Ошибка с логином");
            throw new ValidationException("Логин не должен быть пустым и указывается без пробелов");
        }
        if (user.getName() == null) {
            log.debug("Имени присваивается логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Ошибка с датой");
            throw new ValidationException("Дата не может быть в будущем");
        }
        log.debug("Валидация прошла успешно");
        return "Валидация прошла успешно";
    }
}
