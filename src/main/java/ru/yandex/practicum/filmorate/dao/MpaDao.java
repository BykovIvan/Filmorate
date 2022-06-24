package ru.yandex.practicum.filmorate.dao;

import java.util.Optional;

public interface MpaDao {
    Optional<String> findMpaById(int id);
}
