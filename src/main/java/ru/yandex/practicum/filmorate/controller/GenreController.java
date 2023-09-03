package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Component
@RequestMapping("/genres")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return genreService.getGenreById(id);
    }

    @GetMapping()
    public List<Genre> getAllGenre() {
        return genreService.getAllGenre();
    }

    @PostMapping("/{name}")
    public Genre addGenre(@RequestBody String name) {
        return genreService.addGenre(name);
    }

    @DeleteMapping("/{id}")
    public void removeGenre(@PathVariable int id) {
        genreService.removeGenre(id);
    }
}
