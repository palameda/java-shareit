package ru.practicum.shareit.exception;

/**
 * Исключение DublicateDataException возникает при запросе данных, которые уже зарегистрированы в хранилище
 */
public class DublicateDataException extends RuntimeException {
    public DublicateDataException(String message) {
        super(message);
    }
}
