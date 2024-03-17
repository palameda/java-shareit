package ru.practicum.shareit.exception;

public class BadArgumentException extends RuntimeException {
    public BadArgumentException(String message) {
        super(message);
    }
}
