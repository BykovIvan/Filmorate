package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> mapUsers = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user){
        log.info("Получен запрос к эндпоинту /users. Метод POST");
        checkUser(user);
        if (mapUsers.containsKey(user.getId())){
            return null;
        }
        mapUsers.put(user.getId() ,user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user){
        log.info("Получен запрос к эндпоинту /users. Метод PUT");
        checkUser(user);
        mapUsers.put(user.getId() ,user);
        return user;
    }

    @GetMapping
    public List<User> giveAllFilms(){
        log.info("Получен запрос к эндпоинту /users. Метод GET. Число пользователей - {}", mapUsers.size());
        return new ArrayList<>(mapUsers.values());
    }

    //Проверка валидации фильмов
    private void checkUser(User user){
        if  (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))){
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || !(user.getLogin().contains(" "))){
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null){
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

}
