package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import static ru.yandex.practicum.filmorate.validation.QueryValidation.validateUser;

/*
    Контроллер добавления/обновления/получения пользователей
 */
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
            throw new ValidationException("Пользователя с id = " + user.getId() + " не существует.");
        }
    }

    @GetMapping("/users")
    public ArrayList<User> getUsers() {
        log.debug("Выполнен GET-запрос");
        return new ArrayList<>(idAndUser.values());
    }
}
