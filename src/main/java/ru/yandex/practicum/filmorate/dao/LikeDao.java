package ru.yandex.practicum.filmorate.dao;

public interface LikeDao {
    void addLike(Long filmId, Long userId);
    void deleteLike(Long filmId, Long userId);
    Long findCountLikesByIdFilm(Long filmId);
    boolean containsLikeById(Long filmId, Long userId);
}
