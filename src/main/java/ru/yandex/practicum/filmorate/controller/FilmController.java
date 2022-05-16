package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
    private final int maxLength = 200;
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film create(@RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод POST");
        checkFilm(film);
        mapFilms.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film  update(@RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод PUT");
        checkFilm(film);
        mapFilms.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> giveAllFilms(){
        log.info("Получен запрос к эндпоинту /films. Метод GET. Количество фильмов - {}", mapFilms.size());
        return new ArrayList<>(mapFilms.values());
    }

    //Проверка валидации фильмов
    private void checkFilm(Film film){
        if  (film.getName() == null || film.getName().isBlank()){
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > maxLength || film.getDescription().isBlank()){
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(startFilmDate)){
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().toMinutes() <= 0){
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }


}
