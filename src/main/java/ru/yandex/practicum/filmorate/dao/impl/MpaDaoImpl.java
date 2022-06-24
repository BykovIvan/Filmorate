package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;

import java.util.Optional;

@Component
public class MpaDaoImpl implements MpaDao {
    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Optional<String> findMpaById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from MPA where id = ?", id);
        if(userRows.next()) {
            String name = userRows.getString("name");
            log.info("Найден mpa: {}", id);
            return Optional.of(name);
        } else {
            log.info("Mpa с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}
