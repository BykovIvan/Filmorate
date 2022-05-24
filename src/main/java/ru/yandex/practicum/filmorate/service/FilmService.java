package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final UserStorage userStorage;


    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /**
     * Создание фильма
     * Create movie
     *
     * @param film
     * @return
     */
    public Film create(Film film){
        checkFilm(film);
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
        checkFilm(film);
        if (filmStorage.update(film.getId(), film) != null || film.getId() < 0){
            return filmStorage.getFilmById(film.getId());
        }else {
            throw new ValidationException("Такой фильм не добавлен или id отрицательный");
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
            throw new ValidationException("Нет такого пользователя c ID " + idFilm);
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
            if (userStorage.containsUserById(idUser)){
                if (filmStorage.getFilmById(idFilm).getLikesFromUsers() == null){
                    Set<Long> setForLikes = new HashSet<>();
                    setForLikes.add(idUser);
                    filmStorage.getFilmById(idFilm).setLikesFromUsers(setForLikes);
                }else{
                    filmStorage.getFilmById(idFilm).getLikesFromUsers().add(idUser);
                }
                return filmStorage.getFilmById(idFilm);
            }else {
                throw new RuntimeException("Такокго пользователя не существует!");
            }
        }else {
            throw new RuntimeException("Такого фильма не существует");
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
//            if (userStorage.containsUserById(idUser)){
                if (filmStorage.getFilmById(idFilm).getLikesFromUsers() != null &&
                        filmStorage.getFilmById(idFilm).getLikesFromUsers().contains(idUser)){

                    filmStorage.getFilmById(idFilm).getLikesFromUsers().remove(idUser);

                    return filmStorage.getFilmById(idFilm);
                }else{
                    throw new ValidationException("Список лайков пуст!");
                }
//            }
//            else {
//                throw new ValidationException("Такокго пользователя не существует!");
//            }
        }else {
            throw new ValidationException("Такого фильма не существует!");
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


    /**
     * Проверка валидации фильмов
     * Chech validation films
     *
     * @param film
     */
    private void checkFilm(Film film){
        for (Film getFilm : filmStorage.getAllFilms()) {
            if (film.getName().equals(getFilm.getName()) && film.getReleaseDate().equals(getFilm.getReleaseDate())){
                throw new ValidationException("Такой фильм уже добавлен");
            }
        }
        if (film.getReleaseDate().isBefore(startFilmDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
