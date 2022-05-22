package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    /**
     * Добавление фильма в локальное хранилище
     * Adding the movie to local storage
     * @param film
     * @return
     */
    Film create(Film film);

    /**
     * Обновление фильма в локальном хранилище
     * Update the movie in local storage
     * @param film
     * @return
     */
    Film update(Film film);

    /**
     * Удаление фильма из локального хранилища
     * Remove the movie from local storage
     * @param film
     */
    void delete(Film film);

    /**
     * Получение всех фильмов из хранилища
     * Getting all movies from storage
     * @return
     */
    List<Film> getAllFilms();
}
