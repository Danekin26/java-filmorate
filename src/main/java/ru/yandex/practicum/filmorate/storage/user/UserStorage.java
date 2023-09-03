package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    /*
        Создание пользователя
     */
    User addUser(User user);

    /*
        Обновление пользователя
     */
    User updateUser(User user);

    /*
        Получить список пользователей
     */
    List<User> getUsers();

    /*
        Добавить пользователя в друзья
     */
    User addUserToFriends(int idUser, int idPotentialFriend);

    /*
        Удалить пользователя из друзей
     */
    User removeUserToFriends(int idUser, int idExFriend);

    /*
        Получить список общих друзей пользователей
     */
    List<User> getListCommonFriend(int idUser1, int idUser2);

    /*
        Получить пользователя по id
     */
    User getUserById(int idUser);

    /*
        Получить список друзей
     */
    Set<Integer> collectionFriends(int idUser);
}
