package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingReference;
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
    private Integer id;
    private Integer ownerId;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 512, message = "Размер описания не должен превышать 255 символов")
    private String description;
    @NotNull(message = "Не указан статус для аренды")
    private Boolean available;
    private BookingReference lastBooking;
    private BookingReference nextBooking;
    private List<ResponseComment> comments;
}
