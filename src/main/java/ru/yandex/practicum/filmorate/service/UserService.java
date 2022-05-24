package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс отвечает за операции с пользователями такими как
 * добавление в друзья, удаление из друзей, вывод списка общих друзей
 *
 * The class is responsible for operation with users, like adding friends,
 * removing form friends, get all friends
 */

@Service
public class UserService {

    private Long count = 1L;
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Создание пользователя
     * Create user
     *
     * @param user
     * @return
     */
    public User createUser(User user){
        checkUser(user);
        if (userStorage.create(count, user) != null || user.getId() < 0){
            user.setId(count);
            return userStorage.getUserById(count++);
        }else {
            throw new ValidationException("Такой пользователь уже существует");
        }
    }

    /**
     * Обновление пользователя
     * Update user by ID
     *
     * @param user
     * @return
     */
    public User updateUser(User user){
        checkUser(user);
        if (userStorage.update(user.getId(), user) != null || user.getId() < 0){
            return userStorage.getUserById(user.getId());
        }else {
            throw new ValidationException("Такого пользователя не существует");
        }
    }

    /**
     * Получение всех пользователей
     * Get all user
     *
     * @return
     */
    public List<User> getAllUsers(){
        return userStorage.getAllUsers();
    }


    /**
     * Добавление в друзья пользователя
     * Add friends
     *
     * @param idUser
     * @param idFriend
     * @return
     */
    public User addFriend(Long idUser, Long idFriend){
        if (userStorage.containsUserById(idUser)){
            if (userStorage.containsUserById(idFriend)){
                userStorage.getUserById(idUser).getListIdOfFriends().add(idFriend);
                userStorage.getUserById(idFriend).getListIdOfFriends().add(idUser);
            }else{
                throw new ValidationException("Друг с таким id не существует");
            }
        }else{
            throw new ValidationException("Пользователь с таким id не существует");
        }
        return userStorage.getUserById(idUser);
    }

    /**
     * Удаление из друзей пользователей
     * Delete friends
     *
     * @param idUser
     * @param idFriend
     * @return
     */
    public User deleteFriend(Long idUser, Long idFriend){
        if (userStorage.containsUserById(idUser)){
            if (userStorage.containsUserById(idFriend)){
                userStorage.getUserById(idUser).getListIdOfFriends().remove(idFriend);
                userStorage.getUserById(idFriend).getListIdOfFriends().remove(idUser);
            }else{
                throw new ValidationException("Друг с таким id не существует");
            }
        }else{
            throw new ValidationException("Пользователь с таким id не существует");
        }
        return userStorage.getUserById(idUser);
    }

    /**
     * Получение списка друзей пользователя по ID
     * Get list of friends by ID
     *
     * @param idUser
     * @return
     */
    public List<User> getFriendsOfUser(Long idUser){
        ArrayList<User> listOfFriends = new ArrayList<>();
        if (userStorage.containsUserById(idUser)){
            Set<Long> idOfFriends = userStorage.getUserById(idUser).getListIdOfFriends();
            for (Long idOfFriend : idOfFriends) {
                if (userStorage.containsUserById(idOfFriend)){
                    listOfFriends.add(userStorage.getUserById(idOfFriend));
                }
            }
            return listOfFriends;
        }else{
            throw new ValidationException("Нет такого пользователя");
        }
    }

    /**
     * Получения списка общих друзей
     * Get list of mutual friends
     *
     * @param id
     * @param idOther
     * @return
     */
    public List<User> getOfMutualFriends(Long id, Long idOther){
        ArrayList<User> mutualListFriends = new ArrayList<>();
        if (userStorage.containsUserById(id)){
            if (userStorage.containsUserById(idOther)){
                Set<Long> idOfFirst = userStorage.getUserById(id).getListIdOfFriends();
                Set<Long> idOfSecond = userStorage.getUserById(idOther).getListIdOfFriends();
                Set<Long> idOfMutualFriends = new HashSet<>(idOfFirst);
                idOfMutualFriends.retainAll(idOfSecond);                //получение общих друзей по их спискам
                for (Long idOfMutualFriend : idOfMutualFriends) {
                    if (userStorage.containsUserById(idOfMutualFriend)){
                        mutualListFriends.add(userStorage.getUserById(idOfMutualFriend));
                    }else {
                        throw new ValidationException("Нет такого пользователя в списке");
                    }
                }
                return mutualListFriends;
            }else{
                throw new ValidationException("Нет такого пользователя 2");
            }
        }else {
            throw new ValidationException("Нет такого пользователя 1");
        }

    }


    /**
     * Проверка валидации пользователей
     * Check validation users
     *
     * @param user
     */
    private void checkUser(User user){
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }


}
