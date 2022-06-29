package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
    private final UserStorage userStorage;
    private final LikeDao likeDao;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeDao likeDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeDao = likeDao;
    }

    /**
     * Добавление лайка к фильму
     * Add like Movie
     *
     * @param idFilm
     * @param idUser
     * @return
     */
    public void addLikeFilm(Long idFilm, Long idUser) {
        if (filmStorage.containsFilmById(idFilm)) {
            if (userStorage.containsUserById(idUser)){
                if (!likeDao.containsLikeById(idFilm, idUser)){
                    likeDao.addLike(idFilm, idUser);
                    filmStorage.updateUpRateOfFilms(idFilm);
                }else {
                    throw new NotFoundObjectException("Такой пользователь уже ставил лайк данному фильму");
                }
            }else {
                throw new NotFoundObjectException("Такого пользователя не существует " + idUser);
            }
        }else {
            throw new NotFoundObjectException("Такого фильма не существуе "+ idFilm);
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
    public void deleteLikeFilm(Long idFilm, Long idUser) {
        if (filmStorage.containsFilmById(idFilm)) {
            if (userStorage.containsUserById(idUser)) {
                if (likeDao.containsLikeById(idFilm, idUser)) {
                    likeDao.deleteLike(idFilm, idUser);
                    filmStorage.updateDownRateOfFilms(idFilm);
                } else {
                    throw new NotFoundObjectException("Пользователь уже удалил лайк у данного фильма");
                }
            } else {
                throw new NotFoundObjectException("Такого пользователя не существует " + idUser);
            }
        } else {
            throw new NotFoundObjectException("Такого фильма не существуе " + idFilm);
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
                    .sorted(Comparator.comparingLong(Film::getRate)
                            .reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } else {
            return list.stream()
                    .filter(film -> film.getRate() >= 0)
                    .sorted(Comparator.comparingLong(Film::getRate)
                            .reversed())
                    .limit(count)
                    .collect(Collectors.toList());

        }
    }
}
