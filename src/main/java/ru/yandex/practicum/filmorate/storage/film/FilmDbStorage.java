package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> create(Film film) {
        String insertSql = "INSERT INTO users (name, releaseDate, description, duration, rate, mpa) VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[] { "ID" });
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRate());
            ps.setArray(5, film.get());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Cоздан фильм : {} {}", film.getId(), film.getName());
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        return Optional.empty();
    }

    @Override
    public Optional<Film> getFilmById(Long idFilm) {
        return Optional.empty();
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }

    @Override
    public boolean deleteFilmById(Long idFilm) {
        return false;
    }

    @Override
    public void deleteAllFilms() {

    }

    @Override
    public boolean containsFilmById(Long idFilm) {
        return false;
    }
}