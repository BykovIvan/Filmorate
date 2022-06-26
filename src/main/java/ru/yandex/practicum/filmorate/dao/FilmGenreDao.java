package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreDao {
    void addGenre(Long filmId, Long genreId);
    void deleteGenre(Long filmId, Long genreId);
    boolean containsLikeById(Long filmId, Long userId);
    List<Genre> findGenresByFilms(Long filmId);
}
