package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

/*
    Контроллер добавления/обновления/получения пользователей
 */
@RestController
@Slf4j
@Component
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    /*
        Добавить пользователя
     */
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        log.debug("Выполнен POST-запрос");
        return userStorage.addUser(user);
    }

    /*
        Обновить пользователя
     */
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.debug("Выполнен PUT-запрос");
        return userStorage.updateUser(user);
    }

    /*
        Получить всех пользователей
     */
    @GetMapping("/users")
    public ArrayList<User> getUsers() {
        log.debug("Выполнен GET-запрос");
        return userStorage.getUsers();
    }

    /*
        Добавить в друзья
     */
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.addToFriends(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    /*
        Удалить из друзей
     */
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        return userService.removeToFriends(Integer.parseInt(id), Integer.parseInt(friendId));
    }

    /*
        Получить список друзей пользователя
     */
    @GetMapping("/users/{id}/friends")
    public List<User> getListFriends(@PathVariable String id) {
        return userService.getAllFriendsUser(Integer.parseInt(id));
    }

    /*
        Получить список общих друзей
     */
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getListCommonFriend(@PathVariable String id, @PathVariable String otherId) {
        return userService.getMutualFriendsList(Integer.parseInt(id), Integer.parseInt(otherId));
    }

    /*
        Получить пользователя по id
     */
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {
        return userStorage.getUserById(Integer.parseInt(id));
    }
}
