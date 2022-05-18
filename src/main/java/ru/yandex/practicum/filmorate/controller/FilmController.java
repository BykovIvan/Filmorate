package ru.yandex.practicum.filmorate.controller;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> mapFilms = new HashMap<>();
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос к эндпоинту /films. Метод POST");
        checkFilm(film);
//        if (film.getReleaseDate().isBefore(startFilmDate)) {
//            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
//        }
//        if (film.getDuration().toMinutes() <= 0) {
//            throw new ValidationException("Продолжительность фильма должна быть положительной");
//        }
        mapFilms.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException  {
        log.info("Получен запрос к эндпоинту /films. Метод PUT");
        checkFilm(film);
//        if (film.getReleaseDate().isBefore(startFilmDate)) {
//            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
//        }
//        if (film.getDuration().toMinutes() <= 0) {
//            throw new ValidationException("Продолжительность фильма должна быть положительной");
//        }
        mapFilms.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> giveAllFilms() {
        return new ArrayList<>(mapFilms.values());
    }

    public Map<Integer, Film> getMapFilms() {
        return mapFilms;
    }

    //Проверка валидации фильмов
    private void checkFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(startFilmDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().toMinutes() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }


}
