package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.List;
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
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private List<Genre> genres;
    private Set<Integer> likes;
    private Mpa mpa;
    //private Integer rate;

    public void addLikeForSet(int idUser) {
        likes.add(idUser);
    }

    public void removeLikeForSet(int idUser) {
        likes.remove(idUser);
    }

    public void addGenre(Genre newGenre) {
        genres.add(newGenre);
    }

    public void genreRemove(String removeGenre) {
        genres.remove(removeGenre);
    }
}
