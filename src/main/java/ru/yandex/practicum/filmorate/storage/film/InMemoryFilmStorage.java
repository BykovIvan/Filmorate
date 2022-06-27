package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

/**
 * Класс отвечает за хранение, обновление, удаление и поиск фильмов
 * The class is responsible for storing, updating, deleting and searching for movies
 */

//@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> mapFilms = new HashMap<>();

    @Override
    public Optional<Film> create(Film film) {
        if (mapFilms.containsKey(film.getId())) {
            return Optional.empty();
        }
        mapFilms.put(film.getId(), film);
        return Optional.of(mapFilms.get(film.getId()));
    }

    @Override
    public Optional<Film> update(Film film) {
        if (mapFilms.containsKey(film.getId())) {
            mapFilms.put(film.getId(), film);
            return Optional.of(mapFilms.get(film.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Film> getFilmById(Long idFilm) {
        if (mapFilms.containsKey(idFilm)) {
            return Optional.of(mapFilms.get(idFilm));
        }
        return Optional.empty();
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(mapFilms.values());
    }

    @Override
    public boolean deleteFilmById(Long idFilm) {
        if (mapFilms.containsKey(idFilm)) {
            mapFilms.remove(idFilm);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsFilmById(Long idFilm) {
        return mapFilms.containsKey(idFilm);
    }

    @Override
    public void updateDownRateOfFilms(Long idFilm) {

    }

    @Override
    public void updateUpRateOfFilms(Long idFilm) {

    }


}
