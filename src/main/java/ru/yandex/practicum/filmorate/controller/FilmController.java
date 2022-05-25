package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //Создание фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод POST");
        return filmService.create(film);
    }

    //Обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод PUT");
        return filmService.updateFilm(film);

    }

    //Получение списка всех фильмаов
    @GetMapping
    public List<Film> giveAllFilms() {
        return filmService.getAllFilms();
    }

    //Получение фильма по id
    @GetMapping("/{id}")
    public Film getFilmById(@Valid @PathVariable("id") Long idFilm) {
        return filmService.getFilmById(idFilm);
    }

    //Удаление фильма
//    @DeleteMapping("/{id}")
//    public String deleteFilm(@Valid @PathVariable("id") Long idFilm){
//        log.info("Получен запрос к эндпоинту /films. Метод DELETE");
//        filmService.removeFilmById(idFilm)
//        return "Удален фильм с ID - " + idFilm;
//    }

    //Пользователь ставит лайк фильму
    @PutMapping("{id}/like/{userId}")
    public Film addLikeFilm(@Valid @PathVariable("id") Long idFilm, @Valid @PathVariable("userId") Long idUser){
        return filmService.addLikeFilm(idFilm, idUser);
    }
    //Пользователь удаляет лайк фильму
    @DeleteMapping ("{id}/like/{userId}")
    public Film deleteLikeFilm(@Valid @PathVariable("id") Long idFilm, @Valid @PathVariable("userId") Long idUser){
        return filmService.deleteLikeFilm(idFilm, idUser);
    }


    //Получение списка из первых фильмов по кличеству лайков /films/popular?count={count}
    //возвращает список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public List<Film> getFilmsByRating(@Valid @RequestParam(required = false) Long count){
        return filmService.getFilmsByRating(count);
    }



}
