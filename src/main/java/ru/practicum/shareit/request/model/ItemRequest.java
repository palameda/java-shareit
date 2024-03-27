package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <strong>Запрос на добавление вещи.</strong>
 * Data-класс ItemRequest содержит поля:
 * <ul>
 *     <li>id - уникальный идентификатор запроса, тип {@link Integer}</li>
 *     <li>author - пользователь, оставивший запрос, тип {@link User}</li>
 *     <li>description - текстовое описание запроса, тип {@link String}</li>
 *     <li>created - дата и время создания запроса, тип {@link LocalDateTime}</li>
 * </ul>
 */
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "Поле автор не может быть пустым")
    private User author;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime created;
}
