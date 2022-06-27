package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.Friend;
import ru.yandex.practicum.filmorate.model.user.User;
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
    public void addFriend(Long idUser, Long idFriend) {
        if (userStorage.containsUserById(idUser)) {
            if (userStorage.containsUserById(idFriend)) {
                if (!friendDao.checkFriend(idUser, idFriend)){
                    if (friendDao.checkFriend(idFriend, idUser)){
                        friendDao.changeStatusOnConfirmed(idFriend, idUser);
                        friendDao.addFriend(idUser, idFriend);
                        friendDao.changeStatusOnConfirmed(idUser, idFriend);
                    }else {
                        friendDao.addFriend(idUser, idFriend);
                    }
                }else {
                    throw new ValidationException("Дружба уже существует!");
                }

            } else {
                throw new NotFoundObjectException("Друг с таким id не существует");
            }
        }else {
            throw new NotFoundObjectException("Пользователь с таким id не существует");
        }
    }

    /**
     * Удаление из друзей пользователей
     * Delete friends
     *
     * @param idUser
     * @param idFriend
     * @return
     */
    public void deleteFriend(Long idUser, Long idFriend) {
        if (userStorage.containsUserById(idUser)) {
            if (userStorage.containsUserById(idFriend)) {
                if (friendDao.containsUserInTableById(idUser)){
                    if (friendDao.checkFriend(idFriend, idUser)){
                        friendDao.changeStatusOnDelete(idFriend, idUser);
                    }
                    friendDao.deleteFriend(idUser, idFriend);
                }
            }else {
                throw new NotFoundObjectException("Друг с таким id не существует");
            }
        }else {
            throw new NotFoundObjectException("Пользователь с таким id не существует");
        }
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
            List<Friend> friendsById = friendDao.getAllFriends(idUser);
            for (Friend friend : friendsById) {
                listOfFriends.add(userStorage.getUserById(friend.getFriendId()).get());
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
                Set<Long> idOfFirst = new HashSet<>();
                Set<Long> idOfSecond = new HashSet<>();
                for (Friend allFriend1 : friendDao.getAllFriends(id)) {
                    idOfFirst.add(allFriend1.getFriendId());
                }
                for (Friend allFriend2 : friendDao.getAllFriends(idOther)) {
                    idOfSecond.add(allFriend2.getFriendId());
                }
                if (!idOfFirst.isEmpty() && !idOfSecond.isEmpty()) {
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
                throw new NotFoundObjectException("Нет такого пользователя 2 " + idOther);
            }
        } else {
            throw new NotFoundObjectException("Нет такого пользователя 1 " + id);
        }

    }
}
