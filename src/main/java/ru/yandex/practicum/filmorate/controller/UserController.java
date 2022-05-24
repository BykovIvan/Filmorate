package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту /users. Метод POST");
        return userService.createUser(user);
    }

    //Обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту /users. Метод PUT");
        return userService.updateUser(user);
    }

    //Получение списка всех пользователей
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    //Получение списка всех пользователей
    @GetMapping("/{id}")
    public User getUserById(@Valid @PathVariable("id") Long idUser) {
        return userService.getUserById(idUser);
    }

    //Удаление пользователя
//    @DeleteMapping("/{id}")
//    public String deleteUser(@Valid @PathVariable("id") Long idUser){
//        log.info("Получен запрос к эндпоинту /users. Метод DELETE");
//        userService.removeUserById(idUser)
//        return "Удален пользователь с ID - " + idUser;
//    }

    //Добавление в друзья PUT /users/{id}/friends/{friendId}
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@Valid @PathVariable("id") Long idUser, @PathVariable("friendId") Long idFriend){
        log.info("Получен запрос к эндпоинту /users. Метод PUT");
        return userService.addFriend(idUser, idFriend);
    }

    //Удаление из друзей DELETE /users/{id}/friends/{friendId}
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@Valid @PathVariable("id") Long idUser, @PathVariable("friendId") Long idFriend){
        return userService.deleteFriend(idUser, idFriend);
    }

    //Получение списка пользователей, являющихся его друзьями GET /users/{id}/friends
    @GetMapping("/{id}/friends")
    public List<User> getFriendsOfUser(@Valid @PathVariable("id") Long idUser){
        return userService.getFriendsOfUser(idUser);
    }

    //Получение списка друзей, общих с другими пользователями GET /users/{id}/friends/common/{otherId}
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@Valid @PathVariable("id") Long id, @PathVariable("otherId") Long idOther){
        return userService.getOfMutualFriends(id, idOther);
    }

}
