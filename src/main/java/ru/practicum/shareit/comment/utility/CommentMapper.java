package ru.practicum.shareit.comment.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static Comment requestToComment(RequestComment comment, Item item, User author) {
        return Comment.builder()
                .item(item)
                .author(author)
                .content(comment.getContent())
                .creationDate(LocalDateTime.now())
                .build();
    }

    public static ResponseComment commentToResponse(Comment comment) {
        return ResponseComment.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .author(comment.getAuthor().getName())
                .creationDate(comment.getCreationDate())
                .build();
    }
}
