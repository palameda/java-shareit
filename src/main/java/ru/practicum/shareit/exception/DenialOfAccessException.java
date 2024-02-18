package ru.practicum.shareit.exception;

/**
 * Исключение AccessDeniedException возникает, когда пользователь, не имеющий прав доступа к данным, пытается их изменить.
 */
public class DenialOfAccessException extends RuntimeException {
    public DenialOfAccessException(String message) {
        super(message);
    }
}
