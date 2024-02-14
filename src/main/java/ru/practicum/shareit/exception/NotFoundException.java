package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Исключение NotFoundException возникает при запросе данных, которые отсутствуют в хранилище
 */

@Slf4j
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
