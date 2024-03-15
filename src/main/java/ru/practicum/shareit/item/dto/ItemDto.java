package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingReferenceDto;
import ru.practicum.shareit.comment.dto.ResponseComment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>Data Transfer Object для сущности "Вещь"</p>
 * <p>Dto-класс Item содержит поля:</p>
 * <ul>
 *     <li>itemId - уникальный идентификатор вещи, тип Integer;</li>
 *     <li>name - краткое название, тип String;</li>
 *     <li>description - развёрнутое описание, тип String;</li>
 *     <li>available - статус о доступности вещи для аренды, тип Boolean.</li>
 * </ul>
 */

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor

public class ItemDto {
    private Integer itemId;
    private Integer ownerId;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 255, message = "Размер описания не должен превышать 255 символов")
    private String description;
    @NotNull(message = "Не указан статус для аренды")
    private Boolean available;
    private BookingReferenceDto prevBooking;
    private BookingReferenceDto nextBooking;
    private List<ResponseComment> comments;
}
