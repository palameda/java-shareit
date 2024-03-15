package ru.practicum.shareit.exception;

/**
 * Исключение NotFoundException возникает при запросе данных, которые отсутствуют в хранилище
 */

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
