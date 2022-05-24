package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс отвечает за хранение, обновление, удаление и поиск фильмов
 * The class is responsible for storing, updating, deleting and searching for movies
 */

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Long, Film> mapFilms = new HashMap<>();

    @Override
    public Film create(Long idFilm, Film film) {
        if (mapFilms.containsKey(idFilm)){
            return null;
        }
        mapFilms.put(idFilm, film);
        return mapFilms.get(idFilm);
    }

    @Override
    public Film update(Long idFilm, Film film) {
        if (mapFilms.containsKey(idFilm)){
            mapFilms.put(idFilm, film);
            return mapFilms.get(idFilm);
        }
        return null;
    }

    @Override
    public Film getFilmById(Long idFilm) {
        if (mapFilms.containsKey(idFilm)){
            return mapFilms.get(idFilm);
        }
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(mapFilms.values());
    }

    @Override
    public boolean deleteFilmById(Long idFilm) {
        if (mapFilms.containsKey(idFilm)){
            mapFilms.remove(idFilm);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAllFilms() {
        mapFilms.clear();
    }

    @Override
    public boolean containsFilmById(Long idFilm) {
        return mapFilms.containsKey(idFilm);
    }


}
