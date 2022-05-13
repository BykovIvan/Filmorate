package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();

    @PostMapping
    public User create(@RequestBody User user){
        users.add(user);
        return user;
    }

    @PutMapping
    public User  update(@RequestBody User user){
        users.add(user);
        return user;
    }

    @GetMapping
    public List<User> giveAllFilms(){
        return users;
    }
}
