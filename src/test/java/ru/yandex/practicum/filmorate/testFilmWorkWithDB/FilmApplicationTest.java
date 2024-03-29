package ru.yandex.practicum.filmorate.testFilmWorkWithDB;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmApplicationTest{
    private final FilmStorage filmStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void beforeEach(){
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS MPA(" +
                "id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
                "name varchar(20) NOT NULL)");

        jdbcTemplate.update("INSERT INTO MPA (name)" +
                "VALUES ('G')," +
                "       ('PG')," +
                "       ('PG-13')," +
                "       ('R')," +
                "       ('NC-17')");

        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS films(" +
                "id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
                "name varchar(40) NOT NULL," +
                "release_data date," +
                "description varchar(200)," +
                "duration INTEGER," +
                "rate INTEGER," +
                "mpa INTEGER REFERENCES mpa(id))");


    }

    @AfterEach
    public void afterEach(){
        jdbcTemplate.update("DROP TABLE MPA CASCADE");
        jdbcTemplate.update("DROP TABLE FILMS CASCADE");

    }

    @Test
    public void testCreateFilmAddtoDB(){
        Film filmA = Film.builder()
                .name("Film1")
                .rate(3L)
                .description("Hello film")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        Optional<Film> getFilm = filmStorage.create(filmA);

        assertThat(getFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindFilmById() {
        Film filmA = Film.builder()
                .name("Film1")
                .rate(3L)
                .description("Hello film")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())

                .build();
        filmStorage.create(filmA);

        Optional<Film> filmOptional = filmStorage.getFilmById(1L);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUpdateFilmInToDB(){
        Film filmA = Film.builder()
                .name("Film1")
                .rate(3L)
                .description("Hello film")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(filmA);

        Film filmB = Film.builder()
                .name("Film2")
                .rate(3L)
                .description("Hello film2")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        Optional<Film> getFIlm = filmStorage.create(filmB);

        assertThat(getFIlm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L)
                );
        assertThat(getFIlm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Film2")
                );
        Film filmUpdate = Film.builder()
                .id(2L)
                .name("FilmUpdate")
                .rate(3L)
                .description("Hello film update")
                .releaseDate(LocalDate.of(2001, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();

        filmStorage.update(filmUpdate);

        Optional<Film> filmUpdateOptional = filmStorage.getFilmById(2L);

        assertThat(filmUpdateOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L)
                );
        assertThat(filmUpdateOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "FilmUpdate")
                );
    }

    @Test
    public void testDeleteFilmById(){
        Film filmA = Film.builder()
                .name("Film1")
                .rate(3L)
                .description("Hello film")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(filmA);

        Film filmB = Film.builder()
                .name("Film2")
                .rate(3L)
                .description("Hello film2")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(filmB);

        filmStorage.deleteFilmById(2L);
        Optional<Film> filmOptional = filmStorage.getFilmById(2L);
        assertThat(filmOptional)
                .isEmpty();
        List<Film> listOfFilm = filmStorage.getAllFilms();
        assertThat(listOfFilm.size())
                .isEqualTo(1);

    }

    @Test
    public void testGetAllFilm(){
        Film filmA = Film.builder()
                .name("Film1")
                .rate(3L)
                .description("Hello film")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(filmA);

        Film filmB = Film.builder()
                .name("Film2")
                .rate(3L)
                .description("Hello film2")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(filmB);

        List<Film> listOfFilm = filmStorage.getAllFilms();

        Assert.notEmpty(listOfFilm, "List isEmpty");
        assertThat(listOfFilm.size())
                .isEqualTo(2);

        Optional<Film> filmGetOptional = Optional.of(listOfFilm.get(0));
        assertThat(filmGetOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }


    @Test
    public void testContainsFilmById(){
        Film filmA = Film.builder()
                .name("Film1")
                .rate(3L)
                .description("Hello film")
                .releaseDate(LocalDate.of(2000, 12, 23))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(filmA);
        assertThat(filmStorage.containsFilmById(1L)).isEqualTo(true);
        assertThat(filmStorage.containsFilmById(2L)).isEqualTo(false);
    }
}
