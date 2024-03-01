package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

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
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
    @Column(name = "available", nullable = false)
    private Boolean available;
}
