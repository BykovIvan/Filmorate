package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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

    private final Map<Integer, User> mapUsers = new HashMap<>();

    private int count = 1;

    @Override
    public User create(User user) {
        checkUser(user);
        if (mapUsers.containsKey(user.getId()) || user.getId() < 0) {
            throw new ValidationException("Такой пользователь уже зарегестрирован или id отрицательный");
        }
        user.setId(count);
        count++;
        mapUsers.put(user.getId(), user);
        return user;

    }

    @Override
    public User update(User user) {
        checkUser(user);
        if ((!mapUsers.containsKey(user.getId())) || user.getId() < 0) {
            throw new ValidationException("Такой пользователь не зарегестрирован или id отрицательный");
        }
        mapUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public void remove(User user) {
        if (mapUsers.containsKey(user.getId())){
            mapUsers.remove(user.getId());
        }else {
            throw new ValidationException("Такой пользователь не зарегестрирован");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(mapUsers.values());
    }

    //Проверка валидации фильмов
    private void checkUser(User user) throws ValidationException {

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

}
