package ru.practicum.shareit.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.config.AppConfig;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.db.ItemRequestDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Import(AppConfig.class)
public class ItemRepositoryTest {
    @Autowired
    private ItemRequestDbRepository itemRequestRepository;

    @Autowired
    private UserDbRepository userRepository;

    @Autowired
    private ItemDbRepository itemRepository;

    private User requestAuthor1;
    private User requestAuthor2;
    private User itemHolder;

    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;

    private Item item1;
    private Item item2;
    private Item item3;

    @BeforeEach
    void setup() {
        requestAuthor1 = User.builder()
                .name("author1")
                .email("author1@user.com")
                .build();
        userRepository.save(requestAuthor1);

        requestAuthor2 = User.builder()
                .name("author2")
                .email("author2@user.com")
                .build();
        userRepository.save(requestAuthor2);

        itemHolder = User.builder()
                .name("holder")
                .email("holder@user.com")
                .build();
        userRepository.save(itemHolder);

        itemRequest1 = ItemRequest.builder()
                .author(requestAuthor1)
                .description("description")
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest1);

        itemRequest2 = ItemRequest.builder()
                .author(requestAuthor2)
                .description("description")
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest2);

        itemRequest3 = ItemRequest.builder()
                .author(requestAuthor2)
                .description("description")
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest3);

        item1 = Item.builder()
                .name("item1")
                .description("description")
                .ownerId(itemHolder.getId())
                .available(true)
                .request(itemRequest1)
                .build();
        itemRepository.save(item1);

        item2 = Item.builder()
                .name("item2")
                .description("description")
                .ownerId(itemHolder.getId())
                .available(true)
                .request(itemRequest2)
                .build();
        itemRepository.save(item2);

        item3 = Item.builder()
                .name("item3")
                .description("description")
                .ownerId(itemHolder.getId())
                .available(false)
                .request(itemRequest2)
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
    @DisplayName("Тест метода findAllByOwnerId. Возвращает список всех вещей владельца")
    void testFindAllByOwnerId_whenDataIsValid_thenReturnListOfItems() {
        List<Item> items = itemRepository.findAllByOwnerId(itemHolder.getId());
        Assertions.assertEquals(3, items.size());
    }

    @Test
    @DisplayName("Тест метода findByNameOrDescriptionAndAvailable. Возвращает список всех доступных к бронированию вещей")
    void testFindByNameOrDescriptionAndAvailable_whenDataIsValid_thenReturnListOfItems() {
        List<Item> items = itemRepository.findByNameOrDescriptionAndAvailable("description");
        Assertions.assertEquals(2, items.size());
    }

    @Test
    @DisplayName("Тест метода findAllByRequestIdOrderByIdDesc. Возвращает список всех запрощенных вещей")
    void testFindAllByRequestIdOrderByIdDesc_whenDataIsValid_thenReturnListOfItems() {
        List<Item> items = itemRepository.findAllByRequestIdOrderByIdDesc(itemRequest2.getId());
        Assertions.assertEquals(2, items.size());
    }
}
