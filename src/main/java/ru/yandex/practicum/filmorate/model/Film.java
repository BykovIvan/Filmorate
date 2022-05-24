package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    private Long id;                         //id фильма
    @NonNull
    @NotBlank
    private String name;                    //Название фильма
    @Size(min = 1, max = 200)
    private String description;             //Описание фильма
    private LocalDate releaseDate;          //Дата выхода фильма
    private int duration;                   //Продолжительность фильма
    private Set<Long> likesFromUsers;  //Список пользователей кто поставил лайк фильму и их количество у каждого фильма

}
