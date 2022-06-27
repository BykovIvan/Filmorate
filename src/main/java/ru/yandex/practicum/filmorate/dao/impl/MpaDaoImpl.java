package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class MpaDaoImpl implements MpaDao {

    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }


    @Override
    public Optional<Mpa> findMpaById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from MPA where id = ?", id);
        if(userRows.next()) {
            log.info("Найден MPA: {}", id);
            Mpa mpa = Mpa.builder()
                    .id(id)
                    .name(userRows.getString("name"))
                    .build();
            return Optional.of(mpa);
        } else {
            log.info("MPA с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "SELECT * FROM mpa";
        log.info("Запрос на получение всех mpa.");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public boolean containsMpaById(Long idMpa) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet( "select * from mpa where id = ?", idMpa);
        if (userRows.next()){
            return true;
        } else {
            return false;
        }
    }
}
