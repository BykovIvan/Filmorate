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
    private final Map<Integer, Film> mapFilms = new HashMap<>();
    private int count = 1;
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);;

    @Override
    public Film create(Film film) throws ValidationException {
        checkFilm(film);
        if (mapFilms.containsKey(film.getId()) || film.getId() < 0) {
            throw new ValidationException("Такой фильм уже есть в списке или id отрицательный");
        }
        film.setId(count);
        count++;
        mapFilms.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException  {
        checkFilm(film);
        if ((!mapFilms.containsKey(film.getId())) || film.getId() < 0) {
            throw new ValidationException("Такой фильм не добавлен или id отрицательный");
        }
        mapFilms.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(mapFilms.values());
    }

    @Override
    public void delete(Film film) {
        if (mapFilms.containsKey(film.getId())){
            mapFilms.remove(film.getId());
        }else {
            throw new ValidationException("Такой фильм не добавлен");
        }
    }

    //Проверка валидации фильмов
    private void checkFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(startFilmDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
