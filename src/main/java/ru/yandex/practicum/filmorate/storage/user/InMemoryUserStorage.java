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

    private final Map<Long, User> mapUsers = new HashMap<>();

    private Long count = 1L;

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
    public User searchById(Long idUser) {
        if (mapUsers.containsKey(idUser)){
            return mapUsers.get(idUser);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(mapUsers.values());
    }

//    @Override
//    public Map<Long, User> getMapOfAllUsers() {
//        return null;
//    }

    @Override
    public void deleteById(Long idUser) {
        if (mapUsers.containsKey(idUser)){
            mapUsers.remove(idUser);
        }else {
            throw new ValidationException("Такой пользователь не зарегестрирован");
        }
    }

    @Override
    public void deleteAllUser() {
        mapUsers.clear();
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
