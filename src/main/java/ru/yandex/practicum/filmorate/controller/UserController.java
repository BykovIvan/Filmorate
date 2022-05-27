package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    private Long count = 1L;        //Счетчик для Id

    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
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
        checkUser(user, true);
        if (userStorage.create(count, user) != null) {
            user.setId(count);
            return userStorage.getUserById(count++);
        } else {
            throw new ValidationException("Такой пользователь уже существует");
        }
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
        checkUser(user, false);
        if (userStorage.update(user.getId(), user) != null && user.getId() > 0) {
            return userStorage.getUserById(user.getId());
        } else {
            throw new NotFoundObjectException("Такого пользователя не существует");
        }
    }

    /**
     * Получение списка всех пользователей
     * Get list of users
     *
     * @return
     */
    @GetMapping
    public List<User> allUsers() {
        return userStorage.getAllUsers();
    }

    /**
     * Получение пользователя по его ID
     * Get user by ID
     *
     * @param idUser
     * @return
     */
    @GetMapping("/{id}")
    public User userById(@Valid @PathVariable("id") Long idUser) {
        if (userStorage.containsUserById(idUser)) {
            return userStorage.getUserById(idUser);
        } else {
            throw new NotFoundObjectException("Нет такого пользователя c ID " + idUser);
        }
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
    public List<User> friendsOfUser(@Valid @PathVariable("id") Long idUser) {
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
    public List<User> mutualFriends(@Valid @PathVariable("id") Long id, @PathVariable("otherId") Long idOther) {
        return userService.getOfMutualFriends(id, idOther);
    }

    /**
     * Проверка валидации пользователей
     * Check validation users
     *
     * @param user
     */
    private void checkUser(User user, Boolean isCreated) {
        if (isCreated) {
            for (User getUser : userStorage.getAllUsers()) {
                if (user.getEmail().equals(getUser.getEmail())) {
                    throw new ValidationException("Такой пользователь уже существует");
                }
            }
        }
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

    }

}
