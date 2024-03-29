package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    /**
     * Добавление пользователя в локальное хранилище
     * Adding the User to local storage
     *
     * @param user
     * @return
     */
//    User create(Long idUser, User user);
    Optional<User> create(User user);

    /**
     * Обновление пользователя в локальном хранилище
     * Update the user in local storage
     *
     * @param user
     * @return
     */
    Optional<User> update(User user);

    /**
     * Поиск пользователей по Id
     * Get user by ID
     *
     * @param idUser
     * @return
     */
    Optional<User> getUserById(Long idUser);

    /**
     * Получение всех пользователей из хранилища
     * Getting all users from local storage
     *
     * @return
     */
    List<User> getAllUsers();

    /**
     * Удаление пользователя из локального хранилища
     * Remove the user from local storage
     *
     * @param idUser
     * @return
     */
    boolean deleteUserById(Long idUser);

    /**
     * Провека есть ли в базе пользователь
     * Check user in db
     */

    boolean containsUserById(Long idUser);

}
