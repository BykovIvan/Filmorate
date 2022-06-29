package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;

/**
 * Класс отвечает за хранение, обновление, удаление и поиск пользователей
 * The class is responsible for storing, updating, deleting and searching for users
 */

//@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> mapUsers = new HashMap<>();

    @Override
    public Optional<User> create(User user) {
        if (mapUsers.containsKey(user.getId())) {
            return Optional.empty();
        }
        mapUsers.put(user.getId(), user);
        return Optional.of(mapUsers.get(user.getId()));
    }

    @Override
    public Optional<User> update(User user) {
        if (mapUsers.containsKey(user.getId())) {
            mapUsers.put(user.getId(), user);
            return Optional.of(mapUsers.get(user.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Long idUser) {
        if (mapUsers.containsKey(idUser)) {
            return Optional.of(mapUsers.get(idUser));
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(mapUsers.values());
    }

    @Override
    public boolean deleteUserById(Long idUser) {
        if (mapUsers.containsKey(idUser)) {
            mapUsers.remove(idUser);
            return true;
        }
        return false;

    }

    @Override
    public boolean containsUserById(Long idUser) {
        return mapUsers.containsKey(idUser);
    }

}
