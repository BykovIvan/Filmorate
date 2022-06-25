package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

public interface FriendDao {
    void addFriend(Long id, Long friendId);
    boolean checkFriend(Long id, Long friendId);
    boolean deleteFriend(Long id, Long friendId);
    List<Friend> getAllFriends(Long id);
    void changeStatusOnConfirmed(Long id, Long friendId);
    void changeStatusOnDelete(Long id, Long friendId);
    boolean containsUserInTableById(Long id);

}
