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

    private Long count = 1L;        //Счетчик для Id

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
        checkUser(user, true);
        if (userStorage.create(count, user) != null){
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
        checkUser(user, false);
        if (userStorage.update(user.getId(), user) != null && user.getId() > 0){
            return userStorage.getUserById(user.getId());
        }else {
            throw new NullPointerException("Такого пользователя не существует");
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
     * Получение пользователя по Id
     * Get user by ID
     *
     * @return
     */
    public User getUserById(Long idUser){
        if (userStorage.containsUserById(idUser)){
            return userStorage.getUserById(idUser);
        }else{
            throw new NullPointerException("Нет такого пользователя c ID " + idUser);
        }
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
                //Добавление друга пользователю в друзья
                if (userStorage.getUserById(idUser).getListIdOfFriends() == null){
                    Set<Long> setIdUser = new HashSet<>();
                    setIdUser.add(idFriend);
                    userStorage.getUserById(idUser).setListIdOfFriends(setIdUser);
                }else {
                    userStorage.getUserById(idUser).getListIdOfFriends().add(idFriend);
                }
                //добавление пользователя другу в друзья
                if (userStorage.getUserById(idFriend).getListIdOfFriends() == null){
                    Set<Long> setIdFriend = new HashSet<>();
                    setIdFriend.add(idUser);
                    userStorage.getUserById(idFriend).setListIdOfFriends(setIdFriend);
                }else {
                    userStorage.getUserById(idFriend).getListIdOfFriends().add(idUser);
                }
            }else{
                throw new NullPointerException("Друг с таким id не существует");
            }
        }else{
            throw new NullPointerException("Пользователь с таким id не существует");
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
                throw new NullPointerException("Друг с таким id не существует");
            }
        }else{
            throw new NullPointerException("Пользователь с таким id не существует");
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
            throw new NullPointerException("Нет такого пользователя");
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
                if (idOfFirst != null && idOfSecond != null){
                    Set<Long> idOfMutualFriends = new HashSet<>(idOfFirst);
                    idOfMutualFriends.retainAll(idOfSecond);                //получение общих друзей по их спискам
                    for (Long idOfMutualFriend : idOfMutualFriends) {
                        if (userStorage.containsUserById(idOfMutualFriend)){
                            mutualListFriends.add(userStorage.getUserById(idOfMutualFriend));
                        }else {
                            throw new NullPointerException("Нет такого пользователя в списке");
                        }
                    }
                }
                return mutualListFriends;
            }else{
                throw new NullPointerException("Нет такого пользователя 2");
            }
        }else {
            throw new NullPointerException("Нет такого пользователя 1");
        }

    }


    /**
     * Проверка валидации пользователей
     * Check validation users
     *
     * @param user
     */
    private void checkUser(User user, Boolean isCreated){
        if (isCreated){
            for (User getUser : userStorage.getAllUsers()) {
                if (user.getEmail().equals(getUser.getEmail())){
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
