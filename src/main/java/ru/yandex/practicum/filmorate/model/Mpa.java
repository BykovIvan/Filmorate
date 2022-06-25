package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
//@Setter
//@Getter
public class Mpa {
    private Long id;
    private String name;

//    public Mpa(int id) {
//        this.id = id;
//    }
//
//    public Mpa() {
//    }    //    private String name;

}
