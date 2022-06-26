package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
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
    private Long rate;
    @JsonIgnore
    private Set<Long> likesFromUsers;  //Список пользователей кто поставил лайк фильму и их количество у каждого фильма
    private List<Genre> genres;
    private Mpa mpa;
}
