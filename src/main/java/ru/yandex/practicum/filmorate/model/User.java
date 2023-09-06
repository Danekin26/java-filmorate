package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/*
    Структура пользователя
 */
@Data
@Builder
public class User {
    private Integer id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
    private final Set<Integer> friends;

    public void addToFriend(int idFriend) {
        friends.add(idFriend);
    }

    public void removeToFriend(int idFriend) {
        friends.remove(idFriend);
    }
}
