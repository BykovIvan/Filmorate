package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод POST");
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод PUT");
        return filmStorage.update(film);

    }

    @DeleteMapping
    public String delete(@Valid @RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод DELETE");
        filmStorage.delete(film);
        return "Удален пользователь с ID - " + film.getId();
    }

    @GetMapping
    public List<Film> giveAllFilms() {
        return filmStorage.getAllFilms();
    }



}
