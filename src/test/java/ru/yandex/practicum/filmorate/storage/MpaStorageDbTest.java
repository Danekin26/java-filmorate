package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.impl.MpaStorageDbImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageDbTest {
    @Autowired
    private MpaStorageDbImpl mpaStorage;

    @Test
    public void getMpaByIdTest() {
        Mpa mpa1 = mpaStorage.getMpaById(1);
        assertEquals(new Mpa("G", 1), mpa1);

        Mpa mpa5 = mpaStorage.getMpaById(5);
        assertEquals(new Mpa("NC-17", 5), mpa5);
    }

    @Test
    public void getAllMpaTest() {
        List<Mpa> getAllMpa = mpaStorage.getAllMpa();
        List<Mpa> allMpa = new ArrayList<>();
        Mpa mpa1 = mpaStorage.getMpaById(1);
        Mpa mpa2 = mpaStorage.getMpaById(2);
        Mpa mpa3 = mpaStorage.getMpaById(3);
        Mpa mpa4 = mpaStorage.getMpaById(4);
        Mpa mpa5 = mpaStorage.getMpaById(5);

        allMpa.add(mpa1);
        allMpa.add(mpa2);
        allMpa.add(mpa3);
        allMpa.add(mpa4);
        allMpa.add(mpa5);

        assertEquals(allMpa, getAllMpa);
    }
}
