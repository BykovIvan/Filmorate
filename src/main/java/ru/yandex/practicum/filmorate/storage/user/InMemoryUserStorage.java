package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс отвечает за хранение, обновление, удаление и поиск пользователей
 * The class is responsible for storing, updating, deleting and searching for users
 */

@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> mapUsers = new HashMap<>();

    @Override
    public User create(Long idUser, User user) {
        if (mapUsers.containsKey(idUser)){
            return null;
        }
        mapUsers.put(idUser, user);
        return mapUsers.get(idUser);
    }

    @Override
    public User update(Long idUser, User user) {
        if (mapUsers.containsKey(idUser)){
            mapUsers.put(idUser, user);
            return mapUsers.get(idUser);
        }
        return null;
    }

    @Override
    public User getUserById(Long idUser) {
        if (mapUsers.containsKey(idUser)){
            return mapUsers.get(idUser);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(mapUsers.values());
    }

    @Override
    public boolean deleteUserById(Long idUser) {
        if (mapUsers.containsKey(idUser)){
            mapUsers.remove(idUser);
            return true;
        }
        return false;

    }

    @Override
    public void deleteAllUser() {
        mapUsers.clear();
    }

    @Override
    public boolean containsUserById(Long idUser) {
        return mapUsers.containsKey(idUser);
    }

}
