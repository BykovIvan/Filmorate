package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {

    private int id;                     //ID пользователя
    @NotBlank
    @NonNull
    @Size(min = 1, max = 200)
    @Email()
    private String email;               //email пользователя
    @NotEmpty
    @NonNull
    @Pattern(regexp = "\\S*$")          //не должен содержать пробелы
    private String login;               //Логин пользователя
    private String name;                //Имя пользователя
    private LocalDate birthday;         //Дата рождения
    private Set<Long> idFriends;        //Спискок ид друзей

}
