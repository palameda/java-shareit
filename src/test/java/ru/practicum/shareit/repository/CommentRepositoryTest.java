package ru.practicum.shareit.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.db.CommentDbRepository;
import ru.practicum.shareit.config.AppConfig;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Import(AppConfig.class)
public class CommentRepositoryTest {
    @Autowired
    private UserDbRepository userRepository;

    @Autowired
    private ItemDbRepository itemRepository;

    @Autowired
    private CommentDbRepository commentRepository;

    private User owner;
    private User commentAuthor1;
    private User commentAuthor2;
    private Item item1;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setup() {
        owner = User.builder()
                .name("owner")
                .email("owner@user.com")
                .build();
        userRepository.save(owner);

        commentAuthor1 = User.builder()
                .name("commentAuthor1")
                .email("author1@user.com")
                .build();
        userRepository.save(commentAuthor1);

        commentAuthor2 = User.builder()
                .name("commentAuthor2")
                .email("author2@user.com")
                .build();
        userRepository.save(commentAuthor2);

        item1 = Item.builder()
                .name("item1")
                .description("description")
                .ownerId(owner.getId())
                .available(true)
                .build();
        itemRepository.save(item1);

        comment1 = Comment.builder()
                .author(commentAuthor1)
                .text("text1")
                .item(item1)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment1);

        comment2 = Comment.builder()
                .author(commentAuthor2)
                .text("text2")
                .item(item1)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment2);
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("Тест метода findAllByItemIdOrderById. Возвращает список комментариев")
    void testFindAllByItemIdOrderById_whenInvoked_thenReturnListOfComments() {
        List<Comment> comments = commentRepository.findAllByItemIdOrderById(item1.getId());
        Assertions.assertEquals(2, comments.size());
    }
}
