package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>Data Transfer Object для сущности "Пользователь"</p>
 * <p>Dto-класс User содержит поля:</p>
 * <ul>
 *     <li>id - уникальный идентификатор пользователя, тип Integer;</li>
 *     <li>name - имя пользователя, тип String;</li>
 *     <li>email - адрес электронной почты пользователя, тип String.</li>
 * </ul>
 */

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    @Min(value = 1, message = "Идентификатор должен быть положительным числом")
    private Integer id;
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;
    @NotBlank(message="Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;
}
