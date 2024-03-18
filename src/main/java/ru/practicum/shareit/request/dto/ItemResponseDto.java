package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object для сущности "Запрос на добавление вещи".
 * Dto-класс ItemResponseDto содержит поля:
 * <ul>
 *     <li>id - идентификатор запроса, тип {@link Integer}</li>
 *     <li>description - текстовое описание запроса, тип {@link String}</li>
 *     <li>created - дата и время создания запроса, тип {@link LocalDateTime}</li>
 *     <li>items - список запрошенных вещей, тип {@link ItemDto}</li>
 * </ul>
 */
@Getter
@Builder
@AllArgsConstructor
public class ItemResponseDto {
    private Integer id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
