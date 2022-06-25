package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {

    private GenreDao genreDao;

    public GenreController(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    /**
     * Получение списка всех жанров
     * Get list of genres
     *
     * @return
     */
    @GetMapping
    public List<Genre> allGenres() {
        return genreDao.findAllGenre();
    }

    /**
     * Получение жанра по его ID
     * Get gener by ID
     *
     * @param idGenre
     * @return
     */
    @GetMapping("/{id}")
    public Optional<Genre> userById(@Valid @PathVariable("id") Long idGenre) {
        if (genreDao.containsGenreById(idGenre)) {
            return genreDao.findGenreById(idGenre);
        } else {
            throw new NotFoundObjectException("Нет такого пользователя c ID " + idGenre);
        }
    }
}
