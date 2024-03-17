package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object для сущности "Комментарий"
 * Dto-класс ResponseComment содержит поля:
 * <ul>
 *     <li>id - идентификатор комментария, тип {@link Integer}</li>
 *     <li>text - текст комментария, тип {@link String}</li>
 *     <li>authorName - имя пользователя, который оставил комментарий, тип {@link String}</li>
 *     <li>created - дата и время оставленного комментария, тип {@link LocalDateTime}</li>
 * </ul>
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseComment {
    private Integer id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
