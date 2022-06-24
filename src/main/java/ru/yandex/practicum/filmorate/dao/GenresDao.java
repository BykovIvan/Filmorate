package ru.yandex.practicum.filmorate.dao;

import java.util.Optional;

public interface GenresDao {
    Optional<String> findGenreById(int id);
}
