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

    /**
     * Создание пользователя
     * Create user
     *
     * @param user
     * @return
     */
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту /users. Метод POST");
        return userService.createUser(user);
    }

    /**
     * Обновление пользователя
     * Update user
     *
     * @param user
     * @return
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту /users. Метод PUT");
        return userService.updateUser(user);
    }

    /**
     * Получение списка всех пользователей
     * Get list of users
     *
     * @return
     */
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    /**
     * Получение пользователя по его ID
     * Get user by ID
     *
     * @param idUser
     * @return
     */
    @GetMapping("/{id}")
    public User getUserById(@Valid @PathVariable("id") Long idUser) {
        return userService.getUserById(idUser);
    }

    /**
     * Добавление пользователей друг другу в друзья по их ID
     * Add to friends by ID
     * PUT /users/{id}/friends/{friendId}
     *
     * @param idUser
     * @param idFriend
     * @return
     */
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@Valid @PathVariable("id") Long idUser, @PathVariable("friendId") Long idFriend) {
        log.info("Получен запрос к эндпоинту /users. Метод PUT");
        return userService.addFriend(idUser, idFriend);
    }

    /**
     * Удаление друг друга из друзей по их ID
     * Delete friends by ID
     * DELETE /users/{id}/friends/{friendId}
     *
     * @param idUser
     * @param idFriend
     * @return
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@Valid @PathVariable("id") Long idUser, @PathVariable("friendId") Long idFriend) {
        return userService.deleteFriend(idUser, idFriend);
    }

    /**
     * Получение списка пользователей, являющихся его друзьями
     * Get list of friends
     * GET /users/{id}/friends
     *
     * @param idUser
     * @return
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriendsOfUser(@Valid @PathVariable("id") Long idUser) {
        return userService.getFriendsOfUser(idUser);
    }

    /**
     * Получение списка друзей, общих с другими пользователями
     * Get mutual friends by ID
     * GET /users/{id}/friends/common/{otherId}
     *
     * @param id
     * @param idOther
     * @return
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@Valid @PathVariable("id") Long id, @PathVariable("otherId") Long idOther) {
        return userService.getOfMutualFriends(id, idOther);
    }

    //Удаление пользователя
//    @DeleteMapping("/{id}")
//    public String deleteUser(@Valid @PathVariable("id") Long idUser){
//        log.info("Получен запрос к эндпоинту /users. Метод DELETE");
//        userService.removeUserById(idUser)
//        return "Удален пользователь с ID - " + idUser;
//    }

}
