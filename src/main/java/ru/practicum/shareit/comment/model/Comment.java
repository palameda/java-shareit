package ru.practicum.shareit.comment.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>Data-класс <strong>Comment</strong> содержит информацию о сущности "комментарий" и имеет поля:</p>
 * <ul>
 *     <li>id - уникальный идентификатор комментария, тип {@link Integer};</li>
 *     <li>item - вещь, на которую оставили комментарий, тип {@link Item};</li>
 *     <li>author - автор комментария, тип {@link User};</li>
 *     <li>text - текст комментария, тип {@link String};</li>
 *     <li>created - дата и время оставленного комментария, тип {@link LocalDateTime};</li>
 * </ul>
 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User author;
    @Column(name = "content", nullable = false)
    private String text;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime created;
}
