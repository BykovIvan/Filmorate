package ru.yandex.practicum.filmorate.testUserController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ValidationUsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    User user;

    @Test
    public void createUserResponseOKTest() throws Exception {
        user = User.builder()
                .id(0L)
                .email("bukov123@yadenx.ru")
                .login("Homayak")
                .name("Ivan")
                .birthday(LocalDate.of(2010, 12, 23))
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUserNotEmptyEmailTest() throws Exception {
        user = User.builder()
                .id(0L)
                .email("")
                .login("Homayak")
                .name("Ivan")
                .birthday(LocalDate.of(2010, 12, 23))
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserCorrectEmailTest() throws Exception {
        user = User.builder()
                .id(0L)
                .email("ijsdijdw.fww@")
                .login("Homayak")
                .name("Ivan")
                .birthday(LocalDate.of(2010, 12, 23))
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserEmptyLoginTest() throws Exception {
        user = User.builder()
                .id(0L)
                .email("bukov123@yadenx.ru")
                .login("")
                .name("Ivan")
                .birthday(LocalDate.of(2010, 12, 23))
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithNameWithBlankTest() throws Exception {
        user = User.builder()
                .id(0L)
                .email("bukov123@yadenx.ru")
                .login(" sd ds")
                .name("Ivan")
                .birthday(LocalDate.of(2010, 12, 23))
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithNameIsEmptyTest() throws Exception {
        user = User.builder()
                .id(0L)
                .email("bukov123@yadenx.ru")
                .login("Ivan")
                .name("")
                .birthday(LocalDate.of(2010, 12, 23))
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUserDateOfBirthIsNowTest() throws Exception {
        user = User.builder()
                .id(10L)
                .email("bukov125@yadenx.ru")
                .login("Homayak2")
                .name("Ivan4")
                .birthday(LocalDate.now())
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUserDateOfBirthIsFutureTest() throws Exception {
        user = User.builder()
                .id(0L)
                .email("bukov123@yadenx.ru")
                .login("Homayak")
                .name("Ivan")
                .birthday(LocalDate.of(2022, 12, 23))
                .build();
        String body = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
