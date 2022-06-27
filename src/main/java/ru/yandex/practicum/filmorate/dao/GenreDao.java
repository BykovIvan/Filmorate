package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> findGenreById(Long id);
    List<Genre> findAllGenre();
    boolean containsGenreById(Long idGenre);
}
