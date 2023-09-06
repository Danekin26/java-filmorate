package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.validation.QueryValidation.validateUser;

@Component("databaseUserStorage")
public class UserStorageDbImpl implements UserStorage {
    private JdbcTemplate jdbcTemplate;

    public UserStorageDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        validateUser(user);
        jdbcTemplate.update("INSERT INTO users (email, login, name_user, birthday) VALUES(?,?,?,?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        SqlRowSet usersRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE email = ?", user.getEmail());
        if (usersRowSet.next()) {
            user.setId(usersRowSet.getInt("id_users"));
        } else {
            throw new NotFoundException("Ошибка при создании пользователя с email " + user.getEmail());
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_users = ?", user.getId());
        if (sqlRowSet.next()) {
            jdbcTemplate.update("UPDATE users SET email =?, login = ?, name_user = ?, birthday = ? WHERE id_users = ?",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } else {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
    }

    @Override
    public List<User> getUsers() {
        SqlRowSet usersRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        List<User> allUsers = new ArrayList<>();
        while (usersRowSet.next()) {
            allUsers.add(convertRequestToUser(usersRowSet));
        }
        return allUsers;
    }

    @Override
    public User addUserToFriends(int idUser, int idPotentialFriend) {
        User user = getUserById(idUser);
        User friendUser = getUserById(idPotentialFriend);
        validateUser(user);
        validateUser(friendUser);
        user.addToFriend(idPotentialFriend);
        jdbcTemplate.update("INSERT INTO friend_users (id_person, id_friend) VALUES(?,?)", idUser, idPotentialFriend);
        return user;
    }

    @Override
    public User removeUserToFriends(int idUser, int idExFriend) {
        User user = getUserById(idUser);
        user.removeToFriend(idExFriend);
        jdbcTemplate.update("DELETE FROM friend_users WHERE id_person = ? AND id_friend = ?", idUser, idExFriend);
        return user;
    }

    @Override
    public List<User> getListCommonFriend(int idUser1, int idUser2) {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> friendUser1 = collectionFriends(idUser1);
        Set<Integer> friendUser2 = collectionFriends(idUser2);
        for (Integer idFriend1 : friendUser1) {
            for (Integer idFriend2 : friendUser2) {
                if (Objects.equals(idFriend1, idFriend2)) {
                    commonFriends.add(getUserById(idFriend1));
                }
            }
        }
        return commonFriends;
    }

    @Override
    public User getUserById(int idUser) {
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_users = ?", idUser);
        if (userRow.next()) {
            return convertRequestToUser(userRow);
        } else {
            throw new NotFoundException("Пользователя с id = " + idUser + " не существует");
        }
    }

    @Override
    public Set<Integer> collectionFriends(int idUser) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM friend_users WHERE id_person = ?", idUser);
        Set<Integer> allFriend = new HashSet<>();
        while (sqlRowSet.next()) {
            allFriend.add(sqlRowSet.getInt("id_friend"));
        }
        return allFriend;
    }

    private User convertRequestToUser(SqlRowSet sqlRowSet) {
        int idUser = sqlRowSet.getInt("id_users");
        return User.builder()
                .id(idUser)
                .email(sqlRowSet.getString("email"))
                .login(sqlRowSet.getString("login"))
                .name(sqlRowSet.getString("name_user"))
                .birthday(sqlRowSet.getDate("birthday").toLocalDate())
                .friends(collectionFriends(idUser))
                .build();
    }
}