package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Repository
public class LikeDaoImpl implements LikeDao {

    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void addLike(Long filmId, Long userId) {
        String insertQuery = "insert into LIKES (FILM_ID, USER_ID) values (?, ?)";
        int status = jdbcTemplate.update(insertQuery, filmId, userId);
        if(status != 0){
            log.info("Поставлен лайк фильму с ID {}", filmId);
        }else{
            log.info("Не поставлен лайк фильму с ID  {}", filmId);
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        log.info("Удален лайк у фильма идентификатором {} пользователя {}", filmId, userId);
        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        Object[] args = new Object[] {filmId, userId};
        jdbcTemplate.update(sql, args);
    }

    @Override
    public Long findCountLikesByIdFilm(Long filmId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select COUNT(USER_ID) AS countLike from LIKES where FILM_ID = ?", filmId);
        if(userRows.next()) {
            log.info("Найден фильм: {}", filmId);
            Long likes = userRows.getLong("countLike");
            return likes;
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            return 0L;
        }
    }

    @Override
    public boolean containsLikeById(Long filmId, Long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from LIKES where FILM_ID = ? AND USER_ID = ?", filmId, userId);
        if (userRows.next()){
            return true;
        } else {
            return false;
        }
    }

//    @Override
//    public List<Like> find10BestRateFilms(Long count) {
//        String sql = "SELECT film_id, " +
//                "COUNT(USER_ID) AS COUNT " +
//                "from LIKES " +
//                "GROUP BY film_id " +
//                "ORDER BY COUNT(USER_ID) DESC " +
//                "LIMIT " + count;
//        log.info("Запрос на получение всех популярных фильмов.");
//        return jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs));
//    }
//    private Like makeLike(ResultSet rs) throws SQLException {
//        return Like.builder()
//                .filmId(rs.getLong("film_id"))
//                .build();
//    }
}
