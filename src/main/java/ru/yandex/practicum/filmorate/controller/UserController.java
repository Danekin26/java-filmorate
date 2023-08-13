package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

/*
    Контроллер добавления/обновления/получения пользователей
 */
@RestController
@Slf4j
@Component
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
        Добавить пользователя
     */
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        log.debug("Выполнен POST-запрос");
        return userService.addUser(user);
    }

    /*
        Обновить пользователя
     */
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.debug("Выполнен PUT-запрос");
        return userService.updateUser(user);
    }

    /*
        Получить всех пользователей
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        log.debug("Выполнен GET-запрос");
        return userService.getUsers();
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
        return userService.removeFromFriends(Integer.parseInt(id), Integer.parseInt(friendId));
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
        return userService.getUserById(Integer.parseInt(id));
    }
}
