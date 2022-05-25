package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс отвечает за операции с фильмами, — добавление и удаление лайка,
 * вывод 10 наиболее популярных фильмов по количеству лайков
 *
 *The class is responsible for operations with movies,
 * adding and removing likes, listing the 10 most popular movies
 * by number of likes
 */

@Service
public class FilmService {
    private Long count = 1L;            //Счетчик для id
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28); //Дата начала кинопроизводства

    private final FilmStorage filmStorage;
//    private final UserStorage userStorage;


    public FilmService(FilmStorage filmStorage) {
//    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
//        this.userStorage = userStorage;
    }

    /**
     * Создание фильма
     * Create movie
     *
     * @param film
     * @return
     */
    public Film create(Film film){
        checkFilm(film, true);
        if (filmStorage.create(count, film) != null || film.getId() < 0) {
            film.setId(count);
            return filmStorage.getFilmById(count++);
        }else {
            throw new ValidationException("Такой фильм уже есть в списке или id отрицательный");
        }
    }

    /**
     * Обновление фильма
     * Update film
     *
     * @param film
     * @return
     */
    public Film updateFilm(Film film){
        checkFilm(film, false);
        if (filmStorage.update(film.getId(), film) != null && film.getId() > 0){
            return filmStorage.getFilmById(film.getId());
        }else {
            throw new NullPointerException("Такой фильм не добавлен или id отрицательный");
        }
    }

    /**
     * Получение всех фильмов
     * Get all movies
     *
     * @return
     */
    public List<Film> getAllFilms(){
        return filmStorage.getAllFilms();
    }

    /**
     * Получение фильм по Id
     * Get movie by ID
     *
     * @return
     */
    public Film getFilmById(Long idFilm){
        if (filmStorage.containsFilmById(idFilm)){
            return filmStorage.getFilmById(idFilm);
        }else{
            throw new NullPointerException("Нет такого пользователя c ID " + idFilm);
        }
    }

    /**
     * Добавление лайка к фильму
     * Add like Movie
     *
     * @param idFilm
     * @param idUser
     * @return
     */
    public Film addLikeFilm(Long idFilm, Long idUser){
        if (filmStorage.containsFilmById(idFilm)){
//            if (userStorage.containsUserById(idUser)){   //Проверка на наличия пользователя в хранилище
                if (filmStorage.getFilmById(idFilm).getLikesFromUsers() == null){
                    Set<Long> setForLikes = new HashSet<>();
                    setForLikes.add(idUser);
                    filmStorage.getFilmById(idFilm).setLikesFromUsers(setForLikes);
                    filmStorage.getFilmById(idFilm).setRate(filmStorage.getFilmById(idFilm).getRate() + 1);
                }else if (!(filmStorage.getFilmById(idFilm).getLikesFromUsers().contains(idUser))){
                    filmStorage.getFilmById(idFilm).getLikesFromUsers().add(idUser);
                    filmStorage.getFilmById(idFilm).setRate(filmStorage.getFilmById(idFilm).getRate() + 1);
                }else {
                    throw new NullPointerException("Такой пользователь уже ставил лайк данному фильму");
                }
                return filmStorage.getFilmById(idFilm);
//            }
//            else {
//                throw new RuntimeException("Такокго пользователя не существует!");
//            }
        }else {
            throw new NullPointerException("Такого фильма не существует");
        }
    }

    /**
     * Удаление лайка у фильма
     * Delete like Movie
     *
     * @param idFilm
     * @param idUser
     * @return
     */
    public Film deleteLikeFilm(Long idFilm, Long idUser){
        if (filmStorage.containsFilmById(idFilm)){
//            if (userStorage.containsUserById(idUser)){  //Проверка на наличия пользователя в хранилище
                if (filmStorage.getFilmById(idFilm).getLikesFromUsers() != null &&
                        filmStorage.getFilmById(idFilm).getLikesFromUsers().contains(idUser)){

                    filmStorage.getFilmById(idFilm).getLikesFromUsers().remove(idUser);
                    filmStorage.getFilmById(idFilm).setRate(filmStorage.getFilmById(idFilm).getRate() - 1);

                    return filmStorage.getFilmById(idFilm);
                }else{
                    throw new NullPointerException("Список лайков пуст!");
                }
//            }
//            else {
//                throw new ValidationException("Такокго пользователя не существует!");
//            }
        }else {
            throw new NullPointerException("Такого фильма не существует!");
        }
    }

    //get popular films

    /**
     * Получение фильмов по его популярности
     * Get popular film by rating
     *
     *
     * @param count
     * @return
     */
    public List<Film> getFilmsByRating(Long count){
        List<Film> list = filmStorage.getAllFilms();
        if (count == null){
            return list.stream()
                    .filter(film -> film.getRate() >= 0)
                    .sorted(Comparator.comparingInt(Film::getRate)
                            .reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        }else {
            return list.stream()
                    .filter(film -> film.getRate() >= 0)
                    .sorted(Comparator.comparingInt(Film::getRate)
                            .reversed())
                    .limit(count)
                    .collect(Collectors.toList());

        }
    }

    /**
     * Проверка валидации при создании фильмов
     * Chech validation films
     *
     * @param film
     */
    private void checkFilm(Film film, Boolean isCreate){
        if (isCreate){
            for (Film getFilm : filmStorage.getAllFilms()) {
                if (film.getName().equals(getFilm.getName()) && film.getReleaseDate().equals(film.getReleaseDate())){
                    throw new ValidationException("Такой фильм уже добавлен");
                }
            }
        }
        if (film.getReleaseDate().isBefore(startFilmDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }


//    public void delete(Film film) {
//        if (mapFilms.containsKey(film.getId())){
//            mapFilms.remove(film.getId());
//        }else {
//            throw new ValidationException("Такой фильм не добавлен");
//        }
//    }
//

}
