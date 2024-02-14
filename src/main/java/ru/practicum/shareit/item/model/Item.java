package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p><strong>Вещь</strong> - основная сущность сервиса, вокруг которой строится вся работа.</p>
 * <p>Data-класс Item содержит поля:</p>
 * <ul>
 *     <li>id - уникальный идентификатор вещи, тип Long;</li>
 *     <li>name - краткое название, тип String;</li>
 *     <li>description - развёрнутое описание, тип String;</li>
 *     <li>ownerId - уникальный идентификатор владельца вещи, тип Long</li>
 *     <li>available - статус о доступности вещи для аренды, тип Boolean.</li>
 * </ul>
 */

@Data
@Builder
@AllArgsConstructor
public class Item {
    @Min(value = 1L, message = "Идентификатор должен быть положительным числом")
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 255, message = "Размер описания не должен превышать 255 символов")
    private String description;
    @NotNull(message = "У вещи должен быть владелец")
    @Min(value = 1L, message = "Идентификатор должен быть положительным числом")
    private Long ownerId;
    private Boolean available;
}
