package ru.yandex.practicum.filmorate.dao.impl;

import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public class FilmGenreDaoImpl implements FilmGenreDao {
    @Override
    public void addGenre(Long filmId, Long genreId) {
        
    }

    @Override
    public void deleteGenre(Long filmId, Long genreId) {

    }

    @Override
    public boolean containsLikeById(Long filmId, Long userId) {
        return false;
    }

    @Override
    public List<Genre> findGenresByFilms(Long filmId) {
        return null;
    }
}
