package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();

    @PostMapping
    public Film create(@RequestBody Film film){
        films.add(film);
        return film;
    }

    @PutMapping
    public Film  update(@RequestBody Film film){
        films.add(film);
        return film;
    }

    @GetMapping
    public List<Film> giveAllFilms(){
        return films;
    }


}
