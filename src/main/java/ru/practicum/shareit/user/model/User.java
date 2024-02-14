package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>Data-класс User содержит информацию о сущности "пользователь" и имеет поля:</p>
 * <ul>
 *     <li>id - уникальный идентификатор пользователя, тип Integer;</li>
 *     <li>name - имя пользователя, тип String;</li>
 *     <li>email - адрес электронной почты пользователя, тип String.</li>
 * </ul>
 */

@Data
@Builder
@AllArgsConstructor
public class User {
    @Min(value = 1, message = "Идентификатор должен быть положительным числом")
    private Integer id;
    private String name;
    @NotBlank
    @Email(message = "Некорректный email")
    private String email;
}
