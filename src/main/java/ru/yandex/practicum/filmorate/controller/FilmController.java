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

    /**
     * Создание фильма
     * Create movie
     *
     * @param film
     * @return
     */
    @PostMapping
    public Film create(@Valid @RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод POST");
        return filmService.create(film);
    }

    /**
     * Обновление фильма
     * Update movie
     *
     * @param film
     * @return
     */
    @PutMapping
    public Film update(@Valid @RequestBody Film film){
        log.info("Получен запрос к эндпоинту /films. Метод PUT");
        return filmService.updateFilm(film);
    }

    /**
     * Получение списка всех фильмов
     * Get list of movies
     *
     * @return
     */
    @GetMapping
    public List<Film> giveAllFilms() {
        return filmService.getAllFilms();
    }

    /**
     * Получение фильма по id
     * Get movie by Id
     *
     * @param idFilm
     * @return
     */
    @GetMapping("/{id}")
    public Film getFilmById(@Valid @PathVariable("id") Long idFilm) {
        return filmService.getFilmById(idFilm);
    }

    /**
     * Пользователь ставит лайк фильму
     * User likes the movie
     *
     * @param idFilm
     * @param idUser
     * @return
     */
    @PutMapping("{id}/like/{userId}")
    public Film addLikeFilm(@Valid @PathVariable("id") Long idFilm, @Valid @PathVariable("userId") Long idUser){
        return filmService.addLikeFilm(idFilm, idUser);
    }

    /**
     * Пользователь удаляет лайк у фильма
     * User removes like from a movie
     *
     * @param idFilm
     * @param idUser
     * @return
     */
    @DeleteMapping ("{id}/like/{userId}")
    public Film deleteLikeFilm(@Valid @PathVariable("id") Long idFilm, @Valid @PathVariable("userId") Long idUser){
        return filmService.deleteLikeFilm(idFilm, idUser);
    }

    /**
     * Получение списка из первых фильмов по кличеству лайков
     * возвращает список из первых count фильмов по количеству лайков
     * Getting a list of the first movies by the number of likes
     * returns a list of the first count movies by the number of likes
     * GET /films/popular?count={count}
     *
     * @param count
     * @return
     */
    @GetMapping("/popular")
    public List<Film> getFilmsByRating(@Valid @RequestParam(required = false) Long count){
        return filmService.getFilmsByRating(count);
    }

    //Удаление фильма
//    @DeleteMapping("/{id}")
//    public String deleteFilm(@Valid @PathVariable("id") Long idFilm){
//        log.info("Получен запрос к эндпоинту /films. Метод DELETE");
//        filmService.removeFilmById(idFilm)
//        return "Удален фильм с ID - " + idFilm;
//    }

}
