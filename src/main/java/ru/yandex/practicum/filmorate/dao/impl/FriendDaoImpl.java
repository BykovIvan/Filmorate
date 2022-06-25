package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Component
@Repository
public class FriendDaoImpl implements FriendDao {

    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long id, Long friendId) {

    }

    @Override
    public boolean checkFriend(Long id, Long friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select confirmed from friends " +
                "where user_id = ? and friend_id = ?", id, friendId);
        if(userRows.next()) {
            log.info("Найден пользователь с : {}", id);
            return userRows.getBoolean("confirmed");
        } else {
            throw new NotFoundObjectException("Нет такого пользователя c ID " + id);
        }
//        SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("select confirmed from friends " +
//                "where friend_id = ? and user_id = ?", friendId, id);
//        if(userRows.next()) {
//            log.info("Найден пользователь с : {}", id);
//            return userRows.getBoolean("confirmed");
//        } else {
//            throw new NotFoundObjectException("Нет такого пользователя c ID " + id);
//        }

    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        log.info("Дружба удалена с идентификатором {} {} удалена", id, friendId);
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        Object[] args = new Object[] {id, friendId};
        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public List<Friend> getFriends(Long id) {
        String sql = "SELECT * FROM friends";
        log.info("Запрос на получение всех друзей.");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs));
    }

    private Friend makeFriend(ResultSet rs) throws SQLException {
        return Friend.builder()
                .userId(rs.getLong("user_id"))
                .friendId(rs.getLong("friend_id"))
                .confirmed(rs.getBoolean("confirmed"))
                .build();
    }

}
