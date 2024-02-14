package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>Data Transfer Object для сущности "Вещь"</p>
 * <p>Dto-класс Item содержит поля:</p>
 * <ul>
 *     <li>id - уникальный идентификатор вещи, тип Integer;</li>
 *     <li>name - краткое название, тип String;</li>
 *     <li>description - развёрнутое описание, тип String;</li>
 *     <li>ownerId - уникальный идентификатор владельца вещи, тип Integer</li>
 *     <li>available - статус о доступности вещи для аренды, тип Boolean.</li>
 * </ul>
 */

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    @Min(value = 1, message = "Идентификатор должен быть положительным числом")
    private Integer id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 255, message = "Размер описания не должен превышать 255 символов")
    private String description;
    @NotNull(message = "У вещи должен быть владелец")
    @Min(value = 1, message = "Идентификатор должен быть положительным числом")
    private Integer ownerId;
    private Boolean available;
}
