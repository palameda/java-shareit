package ru.practicum.shareit.exception;

/**
 * Исключение ValidationException возникает, когда данные сущностей не проходят валидацию
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
