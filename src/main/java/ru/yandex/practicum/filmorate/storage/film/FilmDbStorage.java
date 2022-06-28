package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmGenre;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Repository
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmGenreDao filmGenreDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenreDao genreDao, FilmGenreDao filmGenreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.filmGenreDao = filmGenreDao;
    }

    @Override
    public Optional<Film> create(Film film) {
        String insertSql = "INSERT INTO films (name, release_data, description, duration, rate, mpa)" +
                " VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[] { "ID" });
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getRate());
            ps.setLong(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Long getIdFilm = keyHolder.getKey().longValue();
        film.setId(getIdFilm);
        log.info("Фильм создан: {} {}", film.getId(), film.getName());

        if (film.getGenres() != null){
            List<Genre> listOfGenre = film.getGenres();
            for (Genre genre : listOfGenre) {
                filmGenreDao.addGenre(getIdFilm, genre.getId());
            }
        }
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

        if (film.getGenres() == null){
            List<Genre> listOfGenre = getFilmById(film.getId()).get().getGenres();
            if (listOfGenre != null){
                for (Genre genre : listOfGenre) {
                    filmGenreDao.deleteGenre(film.getId(), genre.getId());
                }
            }
            log.info("Обновлен фильм {} {}", film.getId(), film.getName());
            return Optional.of(film);

        } else if (film.getGenres().isEmpty()){
            List<Genre> listOfGenre = getFilmById(film.getId()).get().getGenres();
            if (listOfGenre != null){
                for (Genre genre : listOfGenre) {
                    filmGenreDao.deleteGenre(film.getId(), genre.getId());
                }
            }
            film.setGenres(new ArrayList<>());
            log.info("Обновлен фильм {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            List<Genre> listOfGenre = getFilmById(film.getId()).get().getGenres();//из таблицы
            List<Genre> newListOfGenre = film.getGenres();                         //с объекта
            if (listOfGenre != null){
                for (Genre genre : listOfGenre) {
                    if (filmGenreDao.containsGenreById(film.getId(), genre.getId())){
                        filmGenreDao.deleteGenre(film.getId(), genre.getId());
                    }
                }
                for (Genre genre : newListOfGenre) {
                    if (!filmGenreDao.containsGenreById(film.getId(), genre.getId())){
                        filmGenreDao.addGenre(film.getId(), genre.getId());
                    } else {
                        log.info("Такой жанр уже добавлен: {}", genre.getId());
                    }
                }
            } else {
                for (Genre genre : newListOfGenre) {
                    if (!filmGenreDao.containsGenreById(film.getId(), genre.getId())){
                        filmGenreDao.addGenre(film.getId(), genre.getId());
                    } else {
                        log.info("Такой жанр уже добавлен: {}", genre.getId());
                    }

                }
            }
            log.info("Обновлен фильм {} {}", film.getId(), film.getName());
            return getFilmById(film.getId());
        }

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

            if (!filmGenreDao.findGenresByFilms(idFilm).isEmpty()){
                List<FilmGenre> listOfGenre = filmGenreDao.findGenresByFilms(idFilm);
                List<Genre> genres = new ArrayList<>();
                for (FilmGenre filmGenre : listOfGenre) {
                    genres.add(Genre.builder()
                                    .id(filmGenre.getGenreId())
                                    .name(genreDao.findGenreById(filmGenre.getGenreId()).get().getName())
                                    .build());
                }
                film.setGenres(genres);
            } else {
                film.setGenres(null);
            }
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
        List<Genre> genres = new ArrayList<>();
        if (filmGenreDao.findGenresByFilms(rs.getLong("id")) != null){
            List<FilmGenre> listOfGenre = filmGenreDao.findGenresByFilms(rs.getLong("id"));
            for (FilmGenre filmGenre : listOfGenre) {
                genres.add(Genre.builder()
                        .id(filmGenre.getGenreId())
                        .name(genreDao.findGenreById(filmGenre.getGenreId()).get().getName())
                        .build());
            }
        }
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
                .genres(genres)
                .build();
    }

    @Override
    public void updateDownRateOfFilms(Long idFilm) {
        log.info("Рейтинг у фильма {} повышен.", idFilm);
        String sql = "UPDATE films SET rate = rate + 1 WHERE id = ?";
        Object[] args = new Object[] {idFilm};
        jdbcTemplate.update(sql, args);

    }

    @Override
    public void updateUpRateOfFilms(Long idFilm) {
        log.info("Рейтинг у фильма {} повышен.", idFilm);
        String sql = "UPDATE films SET rate = rate + 1 WHERE id = ?";
        Object[] args = new Object[] {idFilm};
        jdbcTemplate.update(sql, args);
    }
    @Override
    public boolean deleteFilmById(Long idFilm) {
        log.info("Фильм с идентификатором {} удален.", idFilm);
        String sql = "DELETE FROM films WHERE id = ?";
        Object[] args = new Object[] {idFilm};
        return jdbcTemplate.update(sql, args) == 1;
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
