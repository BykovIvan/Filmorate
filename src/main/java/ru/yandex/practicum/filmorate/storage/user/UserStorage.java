package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

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
     * @param user
     * @return
     */
    User update(User user) ;

    /**
     * Удаление пользователя из локального хранилища
     * Remove the user from local storage
     * @param user
     */
    void remove(User user);

    /**
     * Получение всех фильмов из хранилища
     * Getting all users from local storage
     * @return
     */
    List<User> getAllUsers();
}
