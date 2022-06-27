package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    /**
     * Добавление фильма в локальное хранилище
     * Adding the movie to local storage
     *
     * @param film
     * @return
     */
    Optional<Film> create(Film film);

    /**
     * Обновление фильма в локальном хранилище
     * Update the movie in local storage
     *
     * @param film
     * @return
     */
    Optional<Film> update(Film film);

    /**
     * Поиск фильма по Id
     * Get movie by ID
     *
     * @param idFilm
     * @return
     */
    Optional<Film> getFilmById(Long idFilm);

    /**
     * Получение всех фильмов из хранилища
     * Getting all movies from local storage
     *
     * @return
     */
    List<Film> getAllFilms();

    /**
     * Удаление фильма из локального хранилища
     * Remove the movie from local storage
     *
     * @param idFilm
     * @return
     */
    boolean deleteFilmById(Long idFilm);

    /**
     * Проверка содержиться ли фильм по его ID
     * Checking if the movie is contained by his ID
     *
     * @param idFilm
     * @return
     */
    boolean containsFilmById(Long idFilm);

    void updateDownRateOfFilms(Long idFilm);
    void updateUpRateOfFilms(Long idFilm);
}
