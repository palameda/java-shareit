package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * <p><strong>Вещь</strong> - основная сущность сервиса, вокруг которой строится вся работа.</p>
 * <p>Data-класс Item содержит поля:</p>
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
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Integer ownerId;
    private Boolean available;
}
