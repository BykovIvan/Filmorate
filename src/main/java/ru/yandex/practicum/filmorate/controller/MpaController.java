package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private MpaDao mpaDao;

    public MpaController(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    /**
     * Получение списка всех mpa
     * Get list of mpa
     *
     * @return
     */
    @GetMapping
    public List<Mpa> allGenres() {
        return mpaDao.findAllMpa();
    }

    /**
     * Получение mpa по его ID
     * Get mpa by ID
     *
     * @param idMpa
     * @return
     */
    @GetMapping("/{id}")
    public Optional<Mpa> userById(@Valid @PathVariable("id") Long idMpa) {
        if (mpaDao.containsMpaById(idMpa)) {
            return mpaDao.findMpaById(idMpa);
        } else {
            throw new NotFoundObjectException("Нет такого mpa c ID " + idMpa);
        }
    }
}
