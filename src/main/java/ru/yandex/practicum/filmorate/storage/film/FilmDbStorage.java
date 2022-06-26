package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmGenreDao filmGenreDao;


    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenreDao genreDao, FilmGenreDao filmGenreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.filmGenreDao = filmGenreDao;
    }

    @Override
    public Optional<Film> create(Film film) {
        String insertSql = "INSERT INTO films (name, release_data, description, duration, rate, mpa) VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[] { "ID" });
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getRate());
            ps.setLong(6, film.getMpa().getId());
            //Вставить создание списка с жанрами

            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Фильм создан: {} {}", film.getId(), film.getName());
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        jdbcTemplate.update(
"UPDATE films SET name = ?, release_data = ?, description = ?, duration = ?, rate = ?, mpa = ?" +
       "WHERE id = ? ", film.getName(),
                                Date.valueOf(film.getReleaseDate()),
                                film.getDescription(),
                                film.getDuration(),
                                film.getRate(),
                                film.getMpa().getId(),
                                film.getId());
        log.info("Обнавлен фильм {} {}", film.getId(), film.getName());
        return Optional.of(film);
    }

    @Override
    public Optional<Film> getFilmById(Long idFilm) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet( "select * from films where id = ?", idFilm);
        if (filmRows.next()){
            log.info("Найден пользователь: {}", filmRows.getString("name"));
            Film film = Film.builder()
                    .id(idFilm)
                    .name(filmRows.getString("name"))
                    .releaseDate(filmRows.getDate("release_data").toLocalDate())
                    .description(filmRows.getString("description"))
                    .duration(filmRows.getInt("duration"))
                    .rate(filmRows.getLong("rate"))
                    .mpa(Mpa.builder()
                            .id(filmRows.getLong("mpa"))
                            .name(mpaDao.findMpaById(filmRows.getLong("mpa")).get().getName())
                            .build())
                    .build();
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", idFilm);
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        log.info("Запрос на получение всех фильмов.");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .releaseDate(rs.getDate("release_data").toLocalDate())
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .rate(rs.getLong("rate"))
                .mpa(Mpa.builder()
                        .id(rs.getLong("mpa"))
                        .name(mpaDao.findMpaById(rs.getLong("mpa")).get().getName())
                        .build())
//                .genres()
                .build();

    }

    @Override
    public boolean deleteFilmById(Long idFilm) {
        log.info("Фильм с идентификатором {} удален.", idFilm);
        String sql = "DELETE FROM films WHERE id = ?";
        Object[] args = new Object[] {idFilm};
        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public void deleteAllFilms() {

    }

    @Override
    public boolean containsFilmById(Long idFilm) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet( "select * from films where id = ?", idFilm);
        if (userRows.next()){
            return true;
        } else {
            return false;
        }
    }
}
