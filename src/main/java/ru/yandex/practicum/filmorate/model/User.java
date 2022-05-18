package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {

    @PositiveOrZero
    private int id;
    @NotBlank
    @NonNull
    @Size(min = 1, max = 200)
    @Email()
    private String email;
    @NotEmpty
    @NonNull
    @Pattern(regexp = "\\S*$") //не должен содержать пробелы
    private String login;
    private String name;
    private LocalDate birthday;

}
