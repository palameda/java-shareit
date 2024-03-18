package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object для сущности "Комментарий"
 * Dto-класс RequestComment содержит поля:
 * <ul>
 *     <li>itemId - идентификатор вещи, тип {@link Integer}</li>
 *     <li>userId - идентификатор пользователя, тип {@link Integer}</li>
 *     <li>text - текст комментария, тип {@link String}</li>
 * </ul>
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestComment {
    private Integer itemId;
    private Integer userId;
    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 512)
    private String text;
}
