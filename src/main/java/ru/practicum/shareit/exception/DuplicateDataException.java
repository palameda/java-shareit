package ru.practicum.shareit.exception;

/**
 * Исключение DuplicateDataException возникает при запросе данных, которые уже зарегистрированы в хранилище
 */
public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String message) {
        super(message);
    }
}
