package ru.practicum.shareit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.config.AppConfig;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.db.ItemRequestDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Import(AppConfig.class)
public class RequestRepositoryTest {
    private final Pageable RECORDS_AMOUNT = Pageable.ofSize(5);
    @Autowired
    private ItemRequestDbRepository itemRequestRepository;

    @Autowired
    private UserDbRepository userRepository;

    @Autowired
    private ItemDbRepository itemRepository;

    private User requestAuthor1;

    @BeforeEach
    void setup() {

        requestAuthor1 = User.builder()
                .name("author1")
                .email("author1@user.com")
                .build();
        userRepository.save(requestAuthor1);

        User requestAuthor2 = User.builder()
                .name("author2")
                .email("author2@user.com")
                .build();
        userRepository.save(requestAuthor2);

        User itemHolder = User.builder()
                .name("holder")
                .email("holder@user.com")
                .build();
        userRepository.save(itemHolder);

        ItemRequest itemRequest1 = ItemRequest.builder()
                .author(requestAuthor1)
                .description("description")
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest1);

        ItemRequest itemRequest2 = ItemRequest.builder()
                .author(requestAuthor2)
                .description("description")
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest2);

        ItemRequest itemRequest3 = ItemRequest.builder()
                .author(requestAuthor2)
                .description("description")
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest3);

        Item item1 = Item.builder()
                .name("item1")
                .description("description")
                .ownerId(itemHolder.getId())
                .available(true)
                .request(itemRequest1)
                .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
                .name("item2")
                .description("description")
                .ownerId(itemHolder.getId())
                .available(true)
                .request(itemRequest2)
                .build();
        itemRepository.save(item2);

        Item item3 = Item.builder()
                .name("item3")
                .description("description")
                .ownerId(itemHolder.getId())
                .available(true)
                .request(itemRequest3)
                .build();
        itemRepository.save(item3);
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    @DisplayName("Проверка метода findAllByAuthorIdOrderByIdDesc")
    void findAllByAuthorIdOrderByIdDesc_whenInvoked_thenReturnAuthorListOfItemRequests() {
        List<ItemRequest> authorRelatedRequests = itemRequestRepository.findAllByAuthorIdOrderByIdDesc(requestAuthor1.getId());
        assertThat(authorRelatedRequests.size(), equalTo(1));
    }

    @Test
    @DisplayName("Проверка метода findAllByAuthorIdNot")
    void findAllByAuthorIdNot_whenInvoked_thenReturnListOfNotAuthorItemRequests() {
        List<ItemRequest> authorRelatedRequests = itemRequestRepository.findAllByAuthorIdNot(requestAuthor1.getId(), RECORDS_AMOUNT);
        assertThat(authorRelatedRequests.size(), equalTo(2));
    }
}
