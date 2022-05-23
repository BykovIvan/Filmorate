package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Класс отвечает за операции с пользователями,
 * как добавление в друзья, удаление из друзей, вывод списка общих друзей
 *
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
    public User addFriend(Long idUser, Long idFriend){
        Map<Long, User> mapUsers = userStorage.getMapOfAllUsers();
        if (mapUsers.containsKey(idUser)){
            if (mapUsers.containsKey(idFriend)){
                mapUsers.get(idUser).getListIdOfFriends().add(idFriend);
                mapUsers.get(idFriend).getListIdOfFriends().add(idUser);
            }else{
                throw new ValidationException("Друг с таким id не существует");
            }
        }else{
            throw new ValidationException("Пользователь с таким id не существует");
        }
        return mapUsers.get(idUser);
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
        Map<Long, User> mapUsers = userStorage.getMapOfAllUsers();
        if (mapUsers.containsKey(idUser)){
            if (mapUsers.containsKey(idFriend)){
                mapUsers.get(idUser).getListIdOfFriends().remove(idFriend);
                mapUsers.get(idFriend).getListIdOfFriends().remove(idUser);
            }else{
                throw new ValidationException("Друг с таким id не существует");
            }
        }else{
            throw new ValidationException("Пользователь с таким id не существует");
        }
        return mapUsers.get(idUser);
    }

    public List<User> getFriendsOfUser(Long idUser){
        ArrayList<User> listOfFriends = new ArrayList<>();
        Map<Long, User> mapUsers = userStorage.getMapOfAllUsers();
        if (mapUsers.containsKey(idUser)){
            Set<Long> idOfFriends = mapUsers.get(idUser).getListIdOfFriends();
            for (Long idOfFriend : idOfFriends) {
                if (mapUsers.containsKey(idOfFriend)){
                    listOfFriends.add(mapUsers.get(idOfFriend));
                }
            }
            return listOfFriends;
        }else{
            throw new ValidationException("Нет такого пользователя");
        }
    }




}
