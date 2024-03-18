package ru.practicum.shareit.exception;

/**
 * Исключение BadArgumentException возникает, когда происходит обработка некорректных аргументов
 */
public class BadArgumentException extends RuntimeException {
    public BadArgumentException(String message) {
        super(message);
    }
}
