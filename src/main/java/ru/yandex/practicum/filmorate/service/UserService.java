package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

/**
 * Класс отвечает за операции с пользователями такими как
 * добавление в друзья, удаление из друзей, вывод списка общих друзей
 * <p>
 * The class is responsible for operation with users, like adding friends,
 * removing form friends, get all friends
 */

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Добавление в друзья пользователя
     * Add friends
     *
     * @param idUser
     * @param idFriend
     * @return
     */
    public User addFriend(Long idUser, Long idFriend) {
        if (userStorage.containsUserById(idUser)) {
            if (userStorage.containsUserById(idFriend)) {
                if (userStorage.getUserById(idUser).getListIdOfFriends() == null) {   //Добавление друга пользователю в друзья
                    Set<Long> setIdUser = new HashSet<>();
                    setIdUser.add(idFriend);
                    userStorage.getUserById(idUser).setListIdOfFriends(setIdUser);
                } else {
                    userStorage.getUserById(idUser).getListIdOfFriends().add(idFriend);
                }
                if (userStorage.getUserById(idFriend).getListIdOfFriends() == null) {  //добавление пользователя другу в друзья
                    Set<Long> setIdFriend = new HashSet<>();
                    setIdFriend.add(idUser);
                    userStorage.getUserById(idFriend).setListIdOfFriends(setIdFriend);
                } else {
                    userStorage.getUserById(idFriend).getListIdOfFriends().add(idUser);
                }
            } else {
                throw new NotFoundObjectException("Друг с таким id не существует");
            }
        } else {
            throw new NotFoundObjectException("Пользователь с таким id не существует");
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
    public User deleteFriend(Long idUser, Long idFriend) {
        if (userStorage.containsUserById(idUser)) {
            if (userStorage.containsUserById(idFriend)) {
                userStorage.getUserById(idUser).getListIdOfFriends().remove(idFriend);
                userStorage.getUserById(idFriend).getListIdOfFriends().remove(idUser);
            } else {
                throw new NotFoundObjectException("Друг с таким id не существует");
            }
        } else {
            throw new NotFoundObjectException("Пользователь с таким id не существует");
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
    public List<User> getFriendsOfUser(Long idUser) {
        ArrayList<User> listOfFriends = new ArrayList<>();
        if (userStorage.containsUserById(idUser)) {
            Set<Long> idOfFriends = userStorage.getUserById(idUser).getListIdOfFriends();
            for (Long idOfFriend : idOfFriends) {
                if (userStorage.containsUserById(idOfFriend)) {
                    listOfFriends.add(userStorage.getUserById(idOfFriend));
                }
            }
            return listOfFriends;
        } else {
            throw new NotFoundObjectException("Нет такого пользователя");
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
    public List<User> getOfMutualFriends(Long id, Long idOther) {
        ArrayList<User> mutualListFriends = new ArrayList<>();
        if (userStorage.containsUserById(id)) {
            if (userStorage.containsUserById(idOther)) {
                Set<Long> idOfFirst = userStorage.getUserById(id).getListIdOfFriends();
                Set<Long> idOfSecond = userStorage.getUserById(idOther).getListIdOfFriends();
                if (idOfFirst != null && idOfSecond != null) {
                    Set<Long> idOfMutualFriends = new HashSet<>(idOfFirst);
                    idOfMutualFriends.retainAll(idOfSecond);                //получение общих друзей по их спискам
                    for (Long idOfMutualFriend : idOfMutualFriends) {
                        if (userStorage.containsUserById(idOfMutualFriend)) {
                            mutualListFriends.add(userStorage.getUserById(idOfMutualFriend));
                        } else {
                            throw new NotFoundObjectException("Нет такого пользователя в списке");
                        }
                    }
                }
                return mutualListFriends;
            } else {
                throw new NotFoundObjectException("Нет такого пользователя 2");
            }
        } else {
            throw new NotFoundObjectException("Нет такого пользователя 1");
        }
    }
}
