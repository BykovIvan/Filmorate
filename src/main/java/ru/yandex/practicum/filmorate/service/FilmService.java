package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс отвечает за операции с фильмами, — добавление и удаление лайка,
 * вывод 10 наиболее популярных фильмов по количеству лайков
 *
 *The class is responsible for operations with movies,
 * adding and removing likes, listing the 10 most popular movies
 * by number of likes
 */

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private Long count = 1L;
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film){
        checkFilm(film);
        if (filmStorage.create(count, film) != null || film.getId() < 0) {
            film.setId(count);
            return filmStorage.getFilmById(count++);
        }else {
            throw new ValidationException("Такой фильм уже есть в списке или id отрицательный");
        }
    }

    public Film updateFilm(Film film){
        checkFilm(film);
        if (filmStorage.update(film.getId(), film) != null || film.getId() < 0){
            return filmStorage.getFilmById(film.getId());
        }else {
            throw new ValidationException("Такой фильм не добавлен или id отрицательный");
        }
    }

    public List<Film> getAllFilms(){
        return filmStorage.getAllFilms();
    }

//    public void delete(Film film) {
//        if (mapFilms.containsKey(film.getId())){
//            mapFilms.remove(film.getId());
//        }else {
//            throw new ValidationException("Такой фильм не добавлен");
//        }
//    }
//


    /**
     * Проверка валидации фильмов
     * Chech validation films
     *
     * @param film
     */
    private void checkFilm(Film film){
        if (film.getReleaseDate().isBefore(startFilmDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
