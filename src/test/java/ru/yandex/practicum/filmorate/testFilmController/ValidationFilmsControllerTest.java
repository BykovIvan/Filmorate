package ru.yandex.practicum.filmorate.testFilmController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        String body = "{\"id\": 0," +
                "\"name\": \"Hooror\"," +
                " \"description\": \"How to be Hoorror!\"," +
                " \"releaseDate\": \"2000-12-03\"," +
                " \"duration\": 120}";
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void createFilmWithEmptyNameOfFilmTest() throws Exception {
        String body = "{\"id\": 0," +
                "\"name\": \"\"," +
                " \"description\": \"How to be Hoorror!\"," +
                " \"releaseDate\": \"2000-12-03\"," +
                " \"duration\": 120}";
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFilmWithDisMore200CharFilmTest() throws Exception {
        String body = "{\"id\": 0," +
                "\"name\": \"Horror 2\"," +
                " \"description\": \"How to be Hoorror! How to be Hoorror! How to be Hoorror! " +
                "How to be Hoorror! How to be Hoorror! How to be Hoorror! How to be Hoorror! " +
                "How to be Hoorror! How to be Hoorror! How to be Hoorror! How to be Hoorror! " +
                "How to be Hoorror! How to be Hoorror! How to be Hoorror! How to be Hoorror! " +
                "How to be Hoorror! How to be Hoorror! How to be Hoorror! How to be Hoorror! \"," +
                " \"releaseDate\": \"2000-12-03\"," +
                " \"duration\": 120}";
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFilmWithReleaseDateFilmTest() throws Exception {
        String body = "{\"id\": 0," +
                "\"name\": \"Horror 3\"," +
                " \"description\": \"How to be Hoorror!\"," +
                " \"releaseDate\": \"1895-12-28\"," +
                " \"duration\": 120}";
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createFilmWithReleaseDateFilmTest2() throws Exception {
        String body = "{\"id\": 0," +
                "\"name\": \"Hoorror 4\"," +
                " \"description\": \"How to be Hoorror!\"," +
                " \"releaseDate\": \"1895-12-27\"," +
                " \"duration\": 120}";
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void createFilmWithNegativeDurationFilmTest() throws Exception {
        String body = "{\"id\": 0," +
                "\"name\": \"Hooror\"," +
                " \"description\": \"How to be Hoorror!\"," +
                " \"releaseDate\": \"2000-12-03\"," +
                " \"duration\": -120}";
        mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }


}
