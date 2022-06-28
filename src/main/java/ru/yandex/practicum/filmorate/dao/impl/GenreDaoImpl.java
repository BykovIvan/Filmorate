package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class GenreDaoImpl implements GenreDao {
    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findGenreById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from GENRE where id = ?", id);
        if(userRows.next()) {
            log.info("Найден GENRE: {}", id);
            Genre genre = Genre.builder()
                    .id(id)
                    .name(userRows.getString("name"))
                    .build();
            return Optional.of(genre);
        } else {
            log.info("GENRE с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findAllGenre() {
        String sql = "SELECT * FROM genre";
        log.info("Запрос на получение всех жанров.");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public boolean containsGenreById(Long idGenre) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet( "select * from genre where id = ?", idGenre);
        if (userRows.next()){
            return true;
        } else {
            return false;
        }
    }
}
