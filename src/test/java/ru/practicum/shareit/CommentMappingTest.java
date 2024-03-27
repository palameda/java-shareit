package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.utility.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMappingTest {
    User user = User.builder()
            .id(1)
            .name("user")
            .email("user@user.com")
            .build();
    Item item = Item.builder()
            .id(1)
            .name("item")
            .description("description")
            .ownerId(user.getId())
            .available(true)
            .build();
    RequestComment requestComment = new RequestComment(item.getId(), user.getId(), "text");
    Comment comment;
    ResponseComment responseComment;

    @Test
    @DisplayName("Проверка маппинга комментария из запроса в комментарий-модель")
    void testRequestToComment_whenInvoked_thenReturnComment() {
        comment = CommentMapper.requestToComment(requestComment, item, user);
        Assertions.assertEquals(comment.getAuthor().toString(), user.toString());
    }

    @Test
    @DisplayName("Проверка маппинга комментария из модели в комментарий-ответ")
    void testCommentToResponse_whenInvoked_thenReturnResponseComment() {
        comment = CommentMapper.requestToComment(requestComment, item, user);
        responseComment = CommentMapper.commentToResponse(comment);
        Assertions.assertEquals(responseComment.getAuthorName(), user.getName());
    }
}
