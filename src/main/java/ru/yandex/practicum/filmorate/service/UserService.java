package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /*
        Добавить друга
     */
    public User addToFriends(int idUser, int idPotentialFriend) {
        return userStorage.addUserToFriends(idUser, idPotentialFriend);
    }

    /*
        Удалить друга
     */
    public User removeToFriends(int idUser, int idExFriend) {
        return userStorage.removeUserToFriends(idUser, idExFriend);
    }

    /*
        Получить список общих друзей
     */
    public List<User> getMutualFriendsList(int idUser1, int idUser2) {
        return userStorage.getListCommonFriend(idUser1, idUser2);
    }

    /*
        Получить список всех друзей пользователя

        При стриме подается на вход список всех пользователей и они фильтруются по id, указанным в списке друзей пользователя
     */
    public List<User> getAllFriendsUser(int idUser) {
        return userStorage.getUsers().stream()
                .filter(p -> p.getFriends().contains(idUser))
                .collect(Collectors.toList());
    }
}
