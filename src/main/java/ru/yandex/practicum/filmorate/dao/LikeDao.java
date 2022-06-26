package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeDao {
    void addLike(Long filmId, Long userId);
    void deleteLike(Long filmId, Long userId);
    Long findCountLikesByIdFilm(Long filmId);
    boolean containsLikeById(Long filmId, Long userId);
    List<Like> find10BestRateFilms(Long count);
}
