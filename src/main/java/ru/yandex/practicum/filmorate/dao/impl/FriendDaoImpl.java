package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.user.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
@Repository
public class FriendDaoImpl implements FriendDao {

    private final Logger log = LoggerFactory.getLogger(MpaDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String insertQuery = "insert into friends (user_id, friend_id) values (?, ?)";
        int status = jdbcTemplate.update(insertQuery, id, friendId);
        if(status != 0){
            log.info("Подал заявку: ID {}", id);
        }else{
            log.info("Не подал заявку: ID {}", id);
        }
    }

    @Override
    public boolean checkFriend(Long friendId, Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select confirmed from friends " +
                "where user_id = ? and friend_id = ?", friendId, id);
        if(userRows.next()) {
            log.info("Найдена дружба с : {}", id);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        log.info("Дружба удалена с идентификатором {} {} удалена", id, friendId);
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        Object[] args = new Object[] {id, friendId};
        jdbcTemplate.update(sql, args);
    }

    @Override
    public List<Friend> getAllFriends(Long id) {
        String sql = "SELECT * FROM friends where user_id = " + id;
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

    @Override
    public void changeStatusOnConfirmed(Long id, Long friendId) {
        jdbcTemplate.update(
            "UPDATE friends SET confirmed = ? WHERE user_id = ? and friend_id = ?", true, id, friendId);
        log.info("Статус обновлен на подтвержден {}", id);
    }

    @Override
    public void changeStatusOnDelete(Long id, Long friendId) {
        jdbcTemplate.update(
            "UPDATE friends SET confirmed = ? WHERE user_id = ? and friend_id = ?", false, id, friendId);
        log.info("Статус обновлен на удаление {}", id);

    }

    @Override
    public boolean containsUserInTableById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet( "select * from friends where user_id = ?", id);
        if (userRows.next()){
            return true;
        } else {
            return false;
        }
    }

}
