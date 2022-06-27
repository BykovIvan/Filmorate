package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {
    Optional<Mpa> findMpaById(Long id);
    List<Mpa> findAllMpa();
    boolean containsMpaById(Long idMpa);
}
