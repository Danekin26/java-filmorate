package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.impl.UserStorageDbImpl;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageDbTest {
    @Autowired
    private UserStorageDbImpl userStorage;

    @Test
    public void getUserByIdTest() {
        User user = User.builder()
                .id(1)
                .email("testMail@java.com")
                .login("Cats")
                .name("Alex")
                .birthday(LocalDate.parse("1990-01-12"))
                .friends(new HashSet<>())
                .build();
        userStorage.addUser(user);
        User userCreate = userStorage.getUserById(user.getId());

        assertEquals(user, userCreate);
    }

    @Test
    public void updateUserTest() {
        User user1 = User.builder()
                .id(1)
                .email("testMail@java.com")
                .login("Cats")
                .name("Alex")
                .birthday(LocalDate.parse("1990-01-12"))
                .friends(new HashSet<>())
                .build();
        userStorage.addUser(user1);

        User user1Update = User.builder()
                .id(1)
                .email("nomail@java.com")
                .login("noCats")
                .name("noAlex")
                .birthday(LocalDate.parse("2001-12-12"))
                .friends(new HashSet<>())
                .build();

        userStorage.updateUser(user1Update);
        User getUser = userStorage.getUserById(user1.getId());
        assertEquals(getUser, user1Update);
    }
}