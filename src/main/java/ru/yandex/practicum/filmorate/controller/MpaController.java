package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

/*
    Контроллер для управления данными рейтинга
 */
@RestController
@Component
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    @Autowired
    private MpaService mpaService;

    /*
        Получить рейтинг по id
     */
    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        return mpaService.getMpaById(id);
    }

    /*
        Получить все виды рейтинга
     */
    @GetMapping()
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }
}
