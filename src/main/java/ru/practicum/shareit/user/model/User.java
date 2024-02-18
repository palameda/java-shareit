package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

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
public class User {
    private Integer id;
    private String name;
    private String email;
}
