package ru.practicum.shareit.comment.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Утилитарный класс CommentMapper предназначен для конвертации объектов классов {@link RequestComment},
 * {@link RequestComment} и {@link Comment}
 */
@UtilityClass
public class CommentMapper {

    /**
     * Статичный метод requestToComment конвертирует объект-запрос класса {@link RequestComment}
     * в объект класса {@link Comment}
     * @param comment объект-запрос класса {@link RequestComment}
     * @param item объект класса {@link Item}
     * @param author объект класса {@link User}
     * @return объект класса {@link Comment}
     */
    public static Comment requestToComment(RequestComment comment, Item item, User author) {
        return Comment.builder()
                .item(item)
                .author(author)
                .text(comment.getText())
                .created(LocalDateTime.now())
                .build();
    }

    /**
     * Статичный метод requestToComment конвертирует объект класса {@link Comment} в объект-ответ
     * класса {@link ResponseComment}
     * @param comment объект класса {@link Comment}
     * @return объект класса {@link ResponseComment}
     */
    public static ResponseComment commentToResponse(Comment comment) {
        return ResponseComment.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
