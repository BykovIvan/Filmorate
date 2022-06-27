package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
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
                    //Необходим uppdate rate В таблице films и в делит тоже
                    filmStorage.updateUpRateOfFilms(idFilm);
//                    filmStorage.getFilmById(idFilm).get().setRate(likeDao.findCountLikesByIdFilm(idFilm));
//                    filmStorage.getFilmById(idFilm).get().setRate(filmStorage.getFilmById(idFilm).get().getRate() + 1);
                }else {
                    throw new NotFoundObjectException("Такой пользователь уже ставил лайк данному фильму");
                }
            }else {
                throw new NotFoundObjectException("Такого пользователя не существует " + idUser);
            }
        }else {
            throw new NotFoundObjectException("Такого фильма не существуе "+ idFilm);
        }

//        if (filmStorage.containsFilmById(idFilm)) {
//            if (filmStorage.getFilmById(idFilm).get().getLikesFromUsers() == null) {
//                Set<Long> setForLikes = new HashSet<>();
//                setForLikes.add(idUser);
//                filmStorage.getFilmById(idFilm).get().setLikesFromUsers(setForLikes);
//                filmStorage.getFilmById(idFilm).get().setRate(filmStorage.getFilmById(idFilm).get().getRate() + 1);
//            } else if (!(filmStorage.getFilmById(idFilm).get().getLikesFromUsers().contains(idUser))) {
//                filmStorage.getFilmById(idFilm).get().getLikesFromUsers().add(idUser);
//                filmStorage.getFilmById(idFilm).get().setRate(filmStorage.getFilmById(idFilm).get().getRate() + 1);
//            } else {
//                throw new NotFoundObjectException("Такой пользователь уже ставил лайк данному фильму");
//            }
//            return filmStorage.getFilmById(idFilm);
//        } else {
//            throw new NotFoundObjectException("Такого фильма не существует");
//        }

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
//                    filmStorage.getFilmById(idFilm).get().setRate(likeDao.findCountLikesByIdFilm(idFilm));
//                    filmStorage.getFilmById(idFilm).get().setRate(filmStorage.getFilmById(idFilm).get().getRate() - 1);
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
//        if (filmStorage.containsFilmById(idFilm)) {
//            if (filmStorage.getFilmById(idFilm).get().getLikesFromUsers() != null &&
//                    filmStorage.getFilmById(idFilm).get().getLikesFromUsers().contains(idUser)) {
//
//                filmStorage.getFilmById(idFilm).get().getLikesFromUsers().remove(idUser);
//                filmStorage.getFilmById(idFilm).get().setRate(filmStorage.getFilmById(idFilm).get().getRate() - 1);
//
//                return filmStorage.getFilmById(idFilm);
//            } else {
//                throw new NotFoundObjectException("Список лайков пуст!");
//            }
//        } else {
//            throw new NotFoundObjectException("Такого фильма не существует!");
//        }
//    }

    /**
     * Получение фильмов по его популярности
     * Get popular film by rating
     *
     * @param count
     * @return
     */
    public List<Film> getFilmsByRating(Long count) {
//        List<Film> listOfFilms = new ArrayList<>();
//        if (count == null) {
//            List<Like> likesById = likeDao.find10BestRateFilms(10L);
//            for (Like like : likesById) {
//                listOfFilms.add(filmStorage.getFilmById(like.getFilmId()).get());
//            }
//        }else {
//            List<Like> likesById = likeDao.find10BestRateFilms(count);
//            for (Like like : likesById) {
//                listOfFilms.add(filmStorage.getFilmById(like.getFilmId()).get());
//            }
//        }
//        return listOfFilms;


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
