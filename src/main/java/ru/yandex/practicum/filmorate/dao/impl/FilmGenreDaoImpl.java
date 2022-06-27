package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.film.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Repository
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenre(Long filmId, Long genreId) {
        String insertQuery = "insert into FILM_GENRE (film_Id, genre_Id) values (?, ?)";
        int status = jdbcTemplate.update(insertQuery, filmId, genreId);
        if(status != 0){
            log.info("Поставен жарн фильму: ID {}", filmId);
        }else{
            log.info("Не поставен жарн фильму: ID {}", filmId);
        }
    }

    @Override
    public void deleteGenre(Long filmId, Long genreId) {
        log.info("Удален жанр у фильма идентификатором {} {}", filmId, genreId);
        String sql = "DELETE FROM FILM_GENRE WHERE film_Id = ? AND genre_Id = ?";
        Object[] args = new Object[] {filmId, genreId};
        jdbcTemplate.update(sql, args);
    }

    @Override
    public boolean containsGenreById(Long filmId, Long genreId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from FILM_GENRE where film_Id = ? AND genre_Id = ?", filmId, genreId);
        if (userRows.next()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<FilmGenre> findGenresByFilms(Long filmId) {
        String sql = "SELECT * from FILM_GENRE WHERE film_Id = " + filmId;
        log.info("Запрос на получение всех жанров у фильма.");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmGenre(rs));
    }
    private FilmGenre makeFilmGenre(ResultSet rs) throws SQLException {
        return FilmGenre.builder()
                .filmId(rs.getLong("film_id"))
                .genreId(rs.getLong("genre_Id"))
                .build();
    }
}
