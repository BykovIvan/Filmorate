package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

public interface FriendDao {
    void addFriend(Long id, Long friendId);
    boolean checkFriend(Long id, Long friendId);
    boolean deleteFriend(Long id, Long friendId);
    List<Friend> getFriends(Long id);

}
