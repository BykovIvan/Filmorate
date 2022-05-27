package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    /**
     * Добавление пользователя в локальное хранилище
     * Adding the User to local storage
     *
     * @param user
     * @return
     */
    User create(Long idUser, User user);

    /**
     * Обновление пользователя в локальном хранилище
     * Update the user in local storage
     *
     * @param user
     * @return
     */
    User update(Long idUser, User user);

    /**
     * Поиск пользователей по Id
     * Get user by ID
     *
     * @param idUser
     * @return
     */
    User getUserById(Long idUser);

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
     * Удаление всех пользователей
     * Delete all users
     */
    void deleteAllUser();

    /**
     * Проверка содержиться ли пользователь по его ID
     * Checking if the user is contained by his ID
     *
     * @param idUser
     * @return
     */
    boolean containsUserById(Long idUser);

}
