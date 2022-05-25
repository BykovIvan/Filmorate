package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    //400 — если ошибка валидации: ValidationException
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final ValidationException e){
        return new ErrorResponse(e.getMessage());
    }

    //404 — для всех ситуаций, если искомый объект не найден
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIncorrectParameterException(final NullPointerException e){
        return new ErrorResponse(e.getMessage());
    }

    //500 — если возникло исключение
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIncorrectParameterException(final Exception e){
        return new ErrorResponse(e.getMessage());
    }

    //доделать ГЕТ популярных+
    //Доделать контроллеры+
    //Доделать ЕрорХандлер+
    //Доделать класс с эксепшином через ЕрорРеспонс
    //Тесты для контроллеров
    //Коментарии у всех подписать


}
