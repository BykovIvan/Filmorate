package ru.yandex.practicum.filmorate.exceptions;

/**
 * Класс исключения возникающий при ошибках заполнения и проверки правильности
 * Exception class that occurs when padding and validation errors occur
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
