package ru.practicum.shareit.Item;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.DenialOfAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.ram.InMemoryItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImplementation;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.ram.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceTest {
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private ItemService itemService;

    private User user1;
    private User user2;
    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private ItemDto itemDto3;

    @BeforeEach
    public void initTest() {
        itemRepository = new InMemoryItemRepository(new ArrayList<>());
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        itemService = new ItemServiceImplementation(itemRepository, userRepository);

        user1 = User.builder()
                .name("User1")
                .email("user1@email.user.com")
                .build();
        userRepository.saveUser(user1);

        user2 = User.builder()
                .name("User2")
                .email("user2@email.user.com")
                .build();
        userRepository.saveUser(user2);

        itemDto1 = ItemDto.builder()
                .name("Лыжи Fischer RCS Skate")
                .description("Plus")
                .available(true)
                .build();

        itemDto2 = ItemDto.builder()
                .name("Лыжи Fischer Carbonlite Skate")
                .description("Plus")
                .available(true)
                .build();

        itemDto3 = ItemDto.builder()
                .name("Лыжи Kastle RX10WC")
                .description("Plus")
                .available(false)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Создание вещи владельцем")
    public void testShouldSaveItemByOwner() {
        Integer itemId1 = itemService.saveItem(itemDto1, user1.getId()).getId();
        Optional<Item> savedItem1 = Optional.of(itemRepository.getById(itemId1));
        Assertions.assertTrue(savedItem1.isPresent(), "Данные о вещи не были сохранены");
        Assertions.assertEquals(savedItem1.get().getId(), 1,
                "Id не совападает с ожидаемым значением"
        );
        Assertions.assertEquals(savedItem1.get().getName(), "Лыжи Fischer RCS Skate",
                "Название не совападает с ожидаемым значением"
        );
        Assertions.assertEquals(savedItem1.get().getDescription(), "Plus",
                "Описание не совападает с ожидаемым значением"
        );
        Assertions.assertEquals(savedItem1.get().getAvailable(), true,
                "Доступность не совападает с ожидаемым значением"
        );
        Assertions.assertEquals(savedItem1.get().getOwnerId(), user1.getId(),
                "Id владельца не совападает с ожидаемым значением"
        );
    }

    @Test
    @Order(2)
    @DisplayName("Обновление вещи её владельцем")
    public void testShouldUpdateItemByOwner() {
        Integer itemId = itemService.saveItem(itemDto2, user1.getId()).getId();
        Assertions.assertEquals(itemDto2.getDescription(), "Plus",
                "Описание не совападает с ожидаемым значением"
        );
        itemDto2.setId(itemId);
        itemDto2.setDescription("Cold");
        itemService.updateItem(itemDto2,user1.getId());
        Assertions.assertEquals(itemDto2.getDescription(), "Cold",
                "Описание не совападает с ожидаемым значением"
        );
    }

    @Test
    @Order(3)
    @DisplayName("Обновление вещи пользоваетем, который не являетеся владельцем")
    public void testShouldNotUpdateIfUserIsNotOwner() {
        Integer itemId = itemService.saveItem(itemDto2, user1.getId()).getId();
        itemDto2.setId(itemId);
        itemDto2.setDescription("Cold");
        DenialOfAccessException exception = Assertions.assertThrows(
                DenialOfAccessException.class,
                () -> itemService.updateItem(itemDto2,user2.getId())
        );
        Assertions.assertEquals(exception.getMessage(),
                String.format("Отказ в доступе. Пользователь с id " +
                        user2.getId() + " не является владельцем вещи " + itemDto2.getName(),
                        "Сообщение об ошибке отличается от ожидаемого")
        );
    }

    @Test
    @Order(4)
    @DisplayName("Получение списка всех вещей пользователя")
    public void testShouldGetListOfUsersItems() {
        Assertions.assertEquals(itemService.findAll(user1.getId()).size(), 0, "Раземеры списков не совпадают");
        itemService.saveItem(itemDto1, user1.getId());
        itemService.saveItem(itemDto2, user1.getId());
        itemService.saveItem(itemDto3, user1.getId());
        List<ItemDto> usersItems = itemService.findAll(user1.getId());
        Assertions.assertEquals(usersItems.size(), 3, "Раземеры списков не совпадают");
    }

    @Test
    @Order(5)
    @DisplayName("Получение вещи по id")
    public void testShouldGetItemById() {
        Integer itemId = itemService.saveItem(itemDto1, user1.getId()).getId();
        Optional<ItemDto> receivedItem = Optional.of(itemService.findById(itemId));
        Assertions.assertTrue(receivedItem.isPresent(), "При запросе вещи по id получен null");
        Assertions.assertEquals(receivedItem.get().getName(), itemDto1.getName(),
                "Названия не совпрадают"
        );
        Assertions.assertEquals(receivedItem.get().getDescription(), itemDto1.getDescription(),
                "Описания не совпрадают"
        );
        Assertions.assertEquals(receivedItem.get().getAvailable(), itemDto1.getAvailable(),
                "Статусы доступности не совпрадают"
        );
    }

    @Test
    @Order(6)
    @DisplayName("Получение вещи по некорректному id")
    public void testShouldNotReturnItemInCaseOfWrongId() {
        ItemDto savedItem = itemService.saveItem(itemDto1, user1.getId());
        Assertions.assertEquals(savedItem.getId(), 1, "Id не совпадают");
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.findById(999)
        );
        Assertions.assertEquals(exception.getMessage(), "Вещь с id 999 не найдена",
                "Сообщение об ошибке отличается от ожидаемого"
        );
    }

    @Test
    @Order(7)
    @DisplayName("Удаление вещи по id её владельцем")
    public void testShouldDeleteItemByOwner() {
        Integer id1 = itemService.saveItem(itemDto1, user1.getId()).getId();
        Integer id2 = itemService.saveItem(itemDto2, user1.getId()).getId();
        Assertions.assertEquals(itemService.findAll(user1.getId()).size(), 2, "Раземеры списков не совпадают");
        itemService.deleteItem(id1, user1.getId());
        Assertions.assertEquals(itemService.findAll(user1.getId()).size(), 1, "Раземеры списков не совпадают");
        itemService.deleteItem(id2, user1.getId());
        Assertions.assertEquals(itemService.findAll(user1.getId()).size(), 0, "Раземеры списков не совпадают");
    }

    @Test
    @Order(8)
    @DisplayName("Удаление вещи по id пользовалем, который не является владельцем")
    public void testShouldNotDeleteIfUserIsNotOwner() {
        Integer id1 = itemService.saveItem(itemDto1, user1.getId()).getId();
        DenialOfAccessException exception = Assertions.assertThrows(
                DenialOfAccessException.class,
                () -> itemService.deleteItem(id1, user2.getId())
        );
        Assertions.assertEquals(exception.getMessage(), String.format("Отказ в доступе. Пользователь с id " +
                        user2.getId() + " не является владельцем вещи " + itemDto1.getName(),
                "Сообщение об ошибке отличается от ожидаемого")
        );
    }

    @Test
    @Order(9)
    @DisplayName("Получение списка вещей по поисковом запросу")
    public void testShouldReturnListOfWantedItemsIfAvailable() {
        itemService.saveItem(itemDto1, user1.getId());
        itemService.saveItem(itemDto2, user1.getId());
        itemService.saveItem(itemDto3, user1.getId());
        List<ItemDto> wantedItems = itemService.seekItem("лЫж");
        Assertions.assertEquals(wantedItems.size(), 2, "Размеры списков не совпадают");
    }
}
