package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
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
    private final FriendDao friendDao;

    public UserService(UserStorage userStorage, FriendDao friendDao) {
        this.userStorage = userStorage;
        this.friendDao = friendDao;
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
                if (userStorage.getUserById(idUser).get().getListIdOfFriends() == null) {   //Добавление друга пользователю в друзья
                    Set<Long> setIdUser = new HashSet<>();
                    setIdUser.add(idFriend);
                    userStorage.getUserById(idUser).get().setListIdOfFriends(setIdUser);
                } else {
                    userStorage.getUserById(idUser).get().getListIdOfFriends().add(idFriend);
                }
                if (userStorage.getUserById(idFriend).get().getListIdOfFriends() == null) {  //добавление пользователя другу в друзья
                    Set<Long> setIdFriend = new HashSet<>();
                    setIdFriend.add(idUser);
                    userStorage.getUserById(idFriend).get().setListIdOfFriends(setIdFriend);
                } else {
                    userStorage.getUserById(idFriend).get().getListIdOfFriends().add(idUser);
                }
            } else {
                throw new NotFoundObjectException("Друг с таким id не существует");
            }
        } else {
            throw new NotFoundObjectException("Пользователь с таким id не существует");
        }
        return userStorage.getUserById(idUser).get();
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
                userStorage.getUserById(idUser).get().getListIdOfFriends().remove(idFriend);
                userStorage.getUserById(idFriend).get().getListIdOfFriends().remove(idUser);
            } else {
                throw new NotFoundObjectException("Друг с таким id не существует");
            }
        } else {
            throw new NotFoundObjectException("Пользователь с таким id не существует");
        }
        return userStorage.getUserById(idUser).get();
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
            Set<Long> idOfFriends = userStorage.getUserById(idUser).get().getListIdOfFriends();
            for (Long idOfFriend : idOfFriends) {
                if (userStorage.containsUserById(idOfFriend)) {
                    listOfFriends.add(userStorage.getUserById(idOfFriend).get());
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
                Set<Long> idOfFirst = userStorage.getUserById(id).get().getListIdOfFriends();
                Set<Long> idOfSecond = userStorage.getUserById(idOther).get().getListIdOfFriends();
                if (idOfFirst != null && idOfSecond != null) {
                    Set<Long> idOfMutualFriends = new HashSet<>(idOfFirst);
                    idOfMutualFriends.retainAll(idOfSecond);                //получение общих друзей по их спискам
                    for (Long idOfMutualFriend : idOfMutualFriends) {
                        if (userStorage.containsUserById(idOfMutualFriend)) {
                            mutualListFriends.add(userStorage.getUserById(idOfMutualFriend).get());
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
