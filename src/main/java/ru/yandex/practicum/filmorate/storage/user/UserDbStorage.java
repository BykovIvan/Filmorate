package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> create(User user) {
        String insertSql = "INSERT INTO users (email, login, name, birthday) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSql, new String[] { "ID" });
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.info("Найден создан: {} {}", user.getId(), user.getName());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                        "WHERE id = ? ", user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());
        log.info("Обнавлен пользователь {} {}", user.getId(), user.getName());
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserById(Long idUser) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet( "select * from users where id = ?", idUser);
        if (userRows.next()){
            log.info("Найден пользователь: {} {}", userRows.getString("email"), userRows.getString("name"));
            User user = User.builder()
                    .id(idUser)
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build();
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", idUser);
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        log.info("Запрос на получение всех пользователей.");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();

    }

    @Override
    public boolean deleteUserById(Long idUser) {
        log.info("Пользователь с идентификатором {} удален.", idUser);
        String sql = "DELETE FROM USERS WHERE id = ?";
        Object[] args = new Object[] {idUser};
        return jdbcTemplate.update(sql, args) == 1;
    }


    @Override
    public boolean containsUserById(Long idUser) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet( "select * from users where id = ?", idUser);
        if (userRows.next()){
            return true;
        } else {
            return false;
        }
    }
}
