package ru.yandex.practicum.filmorate.testFilmController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ValidationFilmsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    Film film;

    @Test
    public void createFilmResponseOKTest() throws Exception {
        film = Film.builder()
                .id(10L)
                .name("Hooror 14")
                .description("This is Horor")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createFilmWithEmptyNameOfFilmTest() throws Exception {
        film = Film.builder()
                .id(0L)
                .name("")
                .description("This is Horor")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(123)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFilmWithBlankNameOfFilmTest() throws Exception {
        film = Film.builder()
                .id(0L)
                .name("  ")
                .description("This is Horor")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(23)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFilmWithDisMore200CharFilmTest() throws Exception {
        film = Film.builder()
                .id(0L)
                .name("Hooror 1")
                .description("This is Horor This is Horor This is Horor This is Horor This is Horor" +
                        " This is Horor This is Horor This is Horor This is Horor This is Horor This is Horor" +
                        " This is Hororv This is Horor This is Horor This is Horor This is Horor This is Horor" +
                        " This is Horor This is Horor This is Horor This is Horor This is Horor")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(56)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFilmWithReleaseDateFilmTest() throws Exception {
        film = Film.builder()
                .id(0L)
                .name("Hooror 1")
                .description("This is Horor")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(78)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createFilmWithReleaseDateFilmTest2() throws Exception {
        film = Film.builder()
                .id(0L)
                .name("Hooror 1")
                .description("This is Horor")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(45)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void createFilmWithNegativeDurationFilmTest() throws Exception {
        film = Film.builder()
                .id(0L)
                .name("Hooror 1")
                .description("This is Horor")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(-90)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void createFilmWithNegativeDurationIsZeroFilmTest() throws Exception {
        film = Film.builder()
                .id(0L)
                .name("Hooror 1")
                .description("This is Horor")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(0)
                .build();
        String body = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}
