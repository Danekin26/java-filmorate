package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
    Структура фильма
 */
@Data
@Builder
public class Film {
    @Setter
    @Getter
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private int rate;
    private final Set<Integer> listOfLikedUsers = new HashSet<>();

    public void addLikeForSet(int idUser) {
        listOfLikedUsers.add(idUser);
    }

    public void removeLikeForSet(int idUser) {
        listOfLikedUsers.remove(idUser);
    }

    public int getListLikes() {
        return listOfLikedUsers.size();
    }
}
