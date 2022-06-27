package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friend {
    private Long userId;
    private Long friendId;
    private Boolean confirmed;
}
