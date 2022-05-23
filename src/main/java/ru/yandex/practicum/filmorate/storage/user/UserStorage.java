package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    /**
     * Добавление пользователя в локальное хранилище
     * Adding the User to local storage
     * @param user
     * @return
     */
    User create(User user) ;

    /**
     * Обновление пользователя в локальном хранилище
     * Update the user in local storage
     *
     * @param user
     * @return
     */
    User update(User user) ;

    /**
     * Поиск пользователей по Id
     * Search user by ID
     *
     * @param idUser
     * @return
     */
    User searchById(Long idUser);

    /**
     * Получение всех фильмов из хранилища
     * Getting all users from local storage
     * @return
     */
    List<User> getAllUsers();

//    /**
//     * Получить список пользователей в мапе
//     * Get map of Users
//     * @return
//     */
//    Map<Long, User> getMapOfAllUsers();

    /**
     * Удаление пользователя из локального хранилища
     * Remove the user from local storage
     * @param idUser
     */
    void deleteById(Long idUser);

    /**
     * Удаление всех пользователей
     * Delete all users
     */
    void deleteAllUser();

}
