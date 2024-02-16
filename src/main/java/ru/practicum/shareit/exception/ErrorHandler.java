package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.info("Код 400 Bad Request - {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleDenialOfAccessException(final DenialOfAccessException exception) {
        log.info("Код 403 Forbidden - {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException exception) {
        log.info("Код 404 Not Found - {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateDataException(final DuplicateDataException exception) {
        log.info("Код 409 Conflict - {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnknownException(final Exception exception) {
        log.info("Код 500 Internal Server Error - {}", exception.getMessage());
        return Map.of("errorMessage", exception.getMessage());
    }
}
