package ru.yandex.practicum.filmorate.testFilmWorkWithDB;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FilmApplicationTest{
    private final UserDbStorage userStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void beforeEach(){
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS users(" +
                " id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
                " email varchar(40) NOT NULL," +
                " login varchar(20) NOT NULL," +
                " name varchar(20)," +
                " birthday date)");
    }

    @AfterEach
    public void afterEach(){
        jdbcTemplate.update("DROP TABLE USERS CASCADE");
    }

    @Test
    public void testCreateUserAddtoDB(){
        User userA = User.builder()
                .name("Hello")
                .email("mail@mail.ru")
                .login("Test")
                .birthday(LocalDate.of(2000, 12, 23))
                .build();
        Optional<User> getUser = userStorage.create(userA);

        assertThat(getUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindUserById() {
        User userA = User.builder()
                .name("Hello")
                .email("mail@mail.ru")
                .login("Test")
                .birthday(LocalDate.of(2000, 12, 23))
                .build();
        userStorage.create(userA);

        Optional<User> userOptional = userStorage.getUserById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUpdateUserInToDB(){
        User userA = User.builder()
                .name("Hello")
                .email("mail@mail.ru")
                .login("Test")
                .birthday(LocalDate.of(2000, 12, 23))
                .build();
        userStorage.create(userA);

        User userB = User.builder()
                .name("Hello2")
                .email("mail2@mail.ru")
                .login("TestFor2")
                .birthday(LocalDate.of(2000, 11, 23))
                .build();
        Optional<User> getUser = userStorage.create(userB);

        assertThat(getUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
        assertThat(getUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Hello2")
                );

        User userUpdate = User.builder()
                .id(2L)
                .name("HelloUpdate")
                .email("mailUpdate@mail.ru")
                .login("TestUpdate")
                .birthday(LocalDate.of(2000, 12, 23))
                .build();

        userStorage.update(userUpdate);

        Optional<User> userUpdateOptional = userStorage.getUserById(2L);

        assertThat(userUpdateOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
        assertThat(userUpdateOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "HelloUpdate")
                );
    }

    @Test
    public void testDeleteUserById(){
        User userA = User.builder()
                .name("Hello")
                .email("mail@mail.ru")
                .login("Test")
                .birthday(LocalDate.of(2000, 12, 23))
                .build();
        userStorage.create(userA);

        User userB = User.builder()
                .name("Hello2")
                .email("mail2@mail.ru")
                .login("TestFor2")
                .birthday(LocalDate.of(2000, 11, 23))
                .build();
        userStorage.create(userB);

        userStorage.deleteUserById(2L);
        Optional<User> userOptional = userStorage.getUserById(2L);
        assertThat(userOptional)
                .isEmpty();
        List<User> listOfUser = userStorage.getAllUsers();
        assertThat(listOfUser.size())
                .isEqualTo(1);

    }

    @Test
    public void testGetAllUser(){
        User userA = User.builder()
                .name("Hello")
                .email("mail@mail.ru")
                .login("Test")
                .birthday(LocalDate.of(2000, 12, 23))
                .build();
        userStorage.create(userA);
        User userB = User.builder()
                .name("HelloD")
                .email("mailD@mail.ru")
                .login("TestD")
                .birthday(LocalDate.of(2000, 11, 23))
                .build();
        userStorage.create(userB);

        List<User> listOfUser = userStorage.getAllUsers();

        Assert.notEmpty(listOfUser, "List isEmpty");
        assertThat(listOfUser.size())
                .isEqualTo(2);

        Optional<User> userGetOptional = Optional.of(listOfUser.get(0));
        assertThat(userGetOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }


    @Test
    public void testContainsUserById(){
        User userA = User.builder()
                .name("Hello")
                .email("mail@mail.ru")
                .login("Test")
                .birthday(LocalDate.of(2000, 12, 23))
                .build();
        userStorage.create(userA);
        assertThat(userStorage.containsUserById(1L)).isEqualTo(true);
        assertThat(userStorage.containsUserById(2L)).isEqualTo(false);
    }
}
