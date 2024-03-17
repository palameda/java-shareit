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
                .text(comment.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public static ResponseComment commentToResponse(Comment comment) {
        return ResponseComment.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
