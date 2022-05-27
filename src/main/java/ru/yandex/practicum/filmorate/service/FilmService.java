package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс отвечает за операции с фильмами, — добавление и удаление лайка,
 * вывод 10 наиболее популярных фильмов по количеству лайков
 * <p>
 * The class is responsible for operations with movies,
 * adding and removing likes, listing the 10 most popular movies
 * by number of likes
 */

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    /**
     * Добавление лайка к фильму
     * Add like Movie
     *
     * @param idFilm
     * @param idUser
     * @return
     */
    public Film addLikeFilm(Long idFilm, Long idUser) {
        if (filmStorage.containsFilmById(idFilm)) {
            if (filmStorage.getFilmById(idFilm).getLikesFromUsers() == null) {
                Set<Long> setForLikes = new HashSet<>();
                setForLikes.add(idUser);
                filmStorage.getFilmById(idFilm).setLikesFromUsers(setForLikes);
                filmStorage.getFilmById(idFilm).setRate(filmStorage.getFilmById(idFilm).getRate() + 1);
            } else if (!(filmStorage.getFilmById(idFilm).getLikesFromUsers().contains(idUser))) {
                filmStorage.getFilmById(idFilm).getLikesFromUsers().add(idUser);
                filmStorage.getFilmById(idFilm).setRate(filmStorage.getFilmById(idFilm).getRate() + 1);
            } else {
                throw new NotFoundObjectException("Такой пользователь уже ставил лайк данному фильму");
            }
            return filmStorage.getFilmById(idFilm);
        } else {
            throw new NotFoundObjectException("Такого фильма не существует");
        }
    }

    /**
     * Удаление лайка у фильма
     * Delete like Movie
     *
     * @param idFilm
     * @param idUser
     * @return
     */
    public Film deleteLikeFilm(Long idFilm, Long idUser) {
        if (filmStorage.containsFilmById(idFilm)) {
            if (filmStorage.getFilmById(idFilm).getLikesFromUsers() != null &&
                    filmStorage.getFilmById(idFilm).getLikesFromUsers().contains(idUser)) {

                filmStorage.getFilmById(idFilm).getLikesFromUsers().remove(idUser);
                filmStorage.getFilmById(idFilm).setRate(filmStorage.getFilmById(idFilm).getRate() - 1);

                return filmStorage.getFilmById(idFilm);
            } else {
                throw new NotFoundObjectException("Список лайков пуст!");
            }
        } else {
            throw new NotFoundObjectException("Такого фильма не существует!");
        }
    }

    /**
     * Получение фильмов по его популярности
     * Get popular film by rating
     *
     * @param count
     * @return
     */
    public List<Film> getFilmsByRating(Long count) {
        List<Film> list = filmStorage.getAllFilms();
        if (count == null) {
            return list.stream()
                    .filter(film -> film.getRate() >= 0)
                    .sorted(Comparator.comparingInt(Film::getRate)
                            .reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            return list.stream()
                    .filter(film -> film.getRate() >= 0)
                    .sorted(Comparator.comparingInt(Film::getRate)
                            .reversed())
                    .limit(count)
                    .collect(Collectors.toList());

        }
    }
}
