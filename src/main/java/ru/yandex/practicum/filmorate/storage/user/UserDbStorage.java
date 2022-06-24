package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
            ps.setString(4, user.getBirthday().format(formatter));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return Optional.of(user);

//        int userid =  jdbcTemplate.update(
//                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
//                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday().format(formatter)
//        );
//        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                        "WHERE id = ? ", user.getEmail(), user.getLogin(), user.getName(), user.getBirthday().format(formatter), user.getId());
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserById(Long idUser) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet( "select * from users where id = ?", idUser);
        if (userRows.next()){
            log.info("Найден пользователь: {} {}", userRows.getString("email"), userRows.getString("name"));
            String strDate = userRows.getString("birthday");
//            LocalDate date = LocalDate.parse(strDate, formatter);
            User user = User.builder()
                    .id(idUser)
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(LocalDate.parse(strDate, formatter))
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
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(LocalDate.parse(rs.getString("birthday"), formatter))
                .build();

    }

    @Override
    public boolean deleteUserById(Long idUser) {
        log.info("Deleting existing user with id = " + idUser);
        String sql = "DELETE FROM USERS WHERE id = ?";
        Object[] args = new Object[] {idUser};
        return jdbcTemplate.update(sql, args) == 1;
    }

    @Override
    public void deleteAllUser() {

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
