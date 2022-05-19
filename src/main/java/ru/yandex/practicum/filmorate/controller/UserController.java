package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
    private int count = 1;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту /users. Метод POST");
        checkUser(user);
        if (mapUsers.containsKey(user.getId()) || user.getId() < 0) {
            throw new ValidationException("Такой пользователь уже зарегестрирован или id отрицательный");
        }
        user.setId(count);
        count++;
        mapUsers.put(user.getId(), user);
        return user;

    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту /users. Метод PUT");
        checkUser(user);
        if ((!mapUsers.containsKey(user.getId())) || user.getId() < 0) {
            throw new ValidationException("Такой пользователь не зарегестрирован или id отрицательный");
        }
        mapUsers.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> giveAllFilms() {
        return new ArrayList<>(mapUsers.values());
    }

    //Проверка валидации фильмов
    private void checkUser(User user) throws ValidationException {

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

}
