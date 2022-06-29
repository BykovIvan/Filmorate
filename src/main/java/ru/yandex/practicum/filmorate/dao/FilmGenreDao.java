package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.film.FilmGenre;

import java.util.List;

public interface FilmGenreDao {
    void addGenre(Long filmId, Long genreId);
    void deleteGenre(Long filmId, Long genreId);
    boolean containsGenreById(Long filmId, Long userId);
    List<FilmGenre> findGenresByFilms(Long filmId);
}
