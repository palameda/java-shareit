package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object для сущности "Запрос на добавление вещи".
 * Dto-класс ItemRequestDto содержит поля:
 * <ul>
 *     <li>userId - идентификатор пользователя, оставившего запрос, тип {@link Integer}</li>
 *     <li>description - текстовое описание запроса, тип {@link String}</li>
 *     <li>created - дата и время создания запроса, тип {@link LocalDateTime}</li>
 * </ul>
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Integer userId;
    @NotBlank(message = "Описание запроса на добаление вещи не может быть пустым")
    @Size(max = 1024)
    private String description;
    private LocalDateTime created = LocalDateTime.now();

    public ItemRequestDto(Integer userId, String description) {
        this.userId = userId;
        this.description = description;
    }
}
