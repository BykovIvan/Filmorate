package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28); //Дата начала кинопроизводства

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    /**
     * Создание фильма
     * Create movie
     *
     * @param film
     * @return
     */
    @PostMapping
    public Optional<Film> create(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту /films. Метод POST");
   //     checkFilm(film, true);
        return filmStorage.create(film);

//        if (filmStorage.create(film).isPresent() || film.getId() < 0) {
//            film.setId(count);
//            return filmStorage.getFilmById(count++);
//        } else {
//            throw new NotFoundObjectException("Такой фильм уже есть в списке или id отрицательный");
//        }
    }

    /**
     * Обновление фильма
     * Update movie
     *
     * @param film
     * @return
     */
    @PutMapping
    public Optional<Film> update(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту /films. Метод PUT");
   //     checkFilm(film, false);
        if (filmStorage.update(film).isPresent() && film.getId() > 0) {
            return filmStorage.getFilmById(film.getId());
        } else {
            throw new NotFoundObjectException("Такой фильм не добавлен или id отрицательный");
        }
    }

    /**
     * Получение списка всех фильмов
     * Get list of movies
     *
     * @return
     */
    @GetMapping
    public List<Film> allFilms() {
        return filmStorage.getAllFilms();
    }

    /**
     * Получение фильма по id
     * Get movie by Id
     *
     * @param idFilm
     * @return
     */
    @GetMapping("/{id}")
    public Optional<Film> filmById(@Valid @PathVariable("id") Long idFilm) {
        if (filmStorage.containsFilmById(idFilm)) {
            return filmStorage.getFilmById(idFilm);
        } else {
            throw new NotFoundObjectException("Нет такого пользователя c ID " + idFilm);
        }
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
    public Optional<Film> addLikeFilm(@Valid @PathVariable("id") Long idFilm, @Valid @PathVariable("userId") Long idUser) {
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
    @DeleteMapping("{id}/like/{userId}")
    public Optional<Film> deleteLikeFilm(@Valid @PathVariable("id") Long idFilm, @Valid @PathVariable("userId") Long idUser) {
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
    public List<Film> filmsByRating(@Valid @RequestParam(required = false) Long count) {
        return filmService.getFilmsByRating(count);
    }

    /**
     * Проверка валидации при создании фильмов
     * Chech validation films
     *
     * @param film
     */
    private void checkFilm(Film film, Boolean isCreate) {
        if (isCreate) {
            for (Film getFilm : filmStorage.getAllFilms()) {
                if (film.getName().equals(getFilm.getName()) && film.getReleaseDate().equals(film.getReleaseDate())) {
                    throw new ValidationException("Такой фильм уже добавлен");
                }
            }
        }
        if (film.getReleaseDate().isBefore(startFilmDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

}
