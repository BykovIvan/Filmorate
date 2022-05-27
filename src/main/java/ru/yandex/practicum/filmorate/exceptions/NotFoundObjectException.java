package ru.yandex.practicum.filmorate.exceptions;

/**
 * Класс исключения возникающий при пустых и отрицательных значениях
 * Exception class for empty and negative values
 */
public class NotFoundObjectException extends RuntimeException{
    public NotFoundObjectException(String message) {
        super(message);
    }
}
