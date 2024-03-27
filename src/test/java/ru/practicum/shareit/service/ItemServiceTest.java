package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.db.CommentDbRepository;
import ru.practicum.shareit.exception.DenialOfAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.item.service.ItemServiceImplementation;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.db.ItemRequestDbRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemDbRepository itemRepository;
    @Mock
    private CommentDbRepository commentRepository;
    @Mock
    private ItemRequestDbRepository itemRequestRepository;
    @Mock
    private UserDbRepository userRepository;
    @Mock
    private BookingDbRepository bookingRepository;
    @InjectMocks
    private ItemServiceImplementation itemService;

    private final UserDto userDto = UserDto.builder()
            .id(1)
            .name("user")
            .email("user@user.com")
            .build();
    private final User user = User.builder()
            .id(1)
            .name("user")
            .email("user@user.com")
            .build();
    private final User booker = User.builder()
            .id(2)
            .name("booker")
            .email("booker@user.com")
            .build();
    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1)
            .author(booker)
            .description("itemRequest")
            .created(LocalDateTime.now())
            .build();
    private final Item item = Item.builder()
            .id(1)
            .name("item")
            .description("description")
            .available(true)
            .ownerId(1)
            .request(itemRequest)
            .build();
    private final Booking booking = Booking.builder()
            .id(1)
            .item(item)
            .booker(booker)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(2))
            .status(Status.APPROVED)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1)
            .name("item")
            .description("description")
            .available(false)
            .ownerId(1)
            .build();
    private final RequestComment requestComment = new RequestComment(1, 2, "comment");
    private final ResponseComment responseComment = new ResponseComment(1, "comment", "author",
            LocalDateTime.now());
    private final Comment comment = new Comment(1, item, booker, "comment", LocalDateTime.now());

    @Test
    @DisplayName("Проверка saveItem для ItemDto без itemRequest")
    void testSaveItem_whenItemDtoWithoutRequestId_thenReturnItemDto() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Assertions.assertEquals(item.getName(), itemService.saveItem(itemDto, user.getId()).getName());
        verify(userRepository, times(1)).findById(anyInt());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("Проверка saveItem для ItemDto c itemRequest")
    void testSaveItem_whenItemDtoWithRequestId_thenReturnItemDto() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        itemDto.setRequestId(1);
        Assertions.assertEquals(item.getName(), itemService.saveItem(itemDto, user.getId()).getName());
        verify(userRepository, times(1)).findById(anyInt());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("Проверка saveItem. Выбрасывает NotFoundException, когда user не найден")
    void testSaveItem_whenUserIsNotFound_thenThrowNotFoundException() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.saveItem(itemDto, user.getId())
        );
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Проверка метода updateItem. Обновляет данные вещи")
    void testUpdateItem_whenDataIsValid_thenReturnUpdatedItem() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        assert item != null;
        item.setDescription(null);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Assertions.assertNull(itemService.updateItem(itemDto, 1).getDescription());
        Assertions.assertThrows(
                DenialOfAccessException.class, () -> itemService.updateItem(itemDto, 2)
        );
    }

    @Test
    @DisplayName("Проверка метода deleteItem. Удаляет данные вещи")
    void testDeleteItem_whenDataIsValid_thenDeleteItem() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        Assertions.assertDoesNotThrow(
                () -> itemService.deleteItem(1, 1)
        );
        verify(userRepository, times(1)).findById(anyInt());
        verify(itemRepository, times(2)).findById(anyInt());
    }

    @Test
    @DisplayName("Проверка метода deleteItem. Выбрасывает DenialOfAccessException, когда пользователь не является владельцем")
    void testDeleteItem_whenUserIsNotOwner_thenThrowDenialOfAccessException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        Assertions.assertThrows(
                DenialOfAccessException.class,
                () -> itemService.deleteItem(1, 2)
        );
        verify(userRepository, times(1)).findById(anyInt());
        verify(itemRepository, times(2)).findById(anyInt());
    }

    @Test
    @DisplayName("Проверка метода deleteItem. Выбрасывает NotFoundException, когда user не найден")
    void testDeleteItem_whenUserNotFound_thenThrowNotFoundException() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.deleteItem(1, 1)
        );
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Проверка метода deleteItem. Выбрасывает NotFoundException, когда item не найдена")
    void testDeleteItem_whenItemNotFound_thenThrowNotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.deleteItem(1, 1)
        );
        verify(userRepository, times(1)).findById(anyInt());
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Проверка метода seekItem для Item. Возвращает item, если запрос содерижит имя вещи")
    void testSeekItem_whenQueryHasItemNameOrDescription_thenReturnItem() {
        when(itemRepository.findByNameOrDescriptionAndAvailable("item")).thenReturn(List.of(item));
        Assertions.assertEquals(1, itemService.seekItem("item").get(0).getId());
        Assertions.assertEquals(0, itemService.seekItem("").size());
        verify(itemRepository, times(2)).findByNameOrDescriptionAndAvailable(anyString());
    }

    @Test
    @DisplayName("Проверка метода findAllByOwnerId. Возвращает item владельца")
    void testFindAll_whenUserIsOwner_thenReturnListOfItems() {
        when(itemRepository.findAllByOwnerId(anyInt())).thenReturn(List.of(item));
        Assertions.assertEquals(itemService.findAll(1).get(0).getDescription(), item.getDescription());
        verify(itemRepository, times(1)).findAllByOwnerId(anyInt());
    }

    @Test
    @DisplayName("Проверка метода findRequestedItems. Возвращает список запрощенных вещей")
    void testFindRequestedItems_whenItemHasRequest_thenReturnListOfRequestedItems() {
        when(itemRepository.findAllByRequestIdOrderByIdDesc(anyInt())).thenReturn(List.of(item));
        Assertions.assertEquals(1, itemService.findRequestedItems(itemRequest.getId()).size());
        verify(itemRepository, times(1)).findAllByRequestIdOrderByIdDesc(anyInt());
    }

    @Test
    @DisplayName("Проверка метода findById. Возвращает item по id")
    void testFindById_whenDataIsValid_thenReturnItemDto() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        assert item != null;
        Assertions.assertEquals(item.getName(), itemService.findById(item.getId(), user.getId()).getName());
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Проверка метода findById. Выбрасывает NotFoundException, когда item не найдена")
    void testFindById_whenItemIsNotFound_thenThrowNotFoundException() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.findById(item.getId(), user.getId())
        );
        verify(itemRepository, times(1)).findById(anyInt());
    }
}
