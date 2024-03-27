package ru.practicum.shareit.service.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImplementation;
import ru.practicum.shareit.item.utility.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.db.ItemRequestDbRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImplementation;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImplementation;
import ru.practicum.shareit.user.utility.UserMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemRequestIntegrationTest {
    private UserService userService;
    private ItemService itemService;
    private ItemRequestDbRepository itemRequestRepository;
    private ItemRequestService itemRequestService;

    private User user;
    private User owner;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(1)
                .name("user")
                .email("user@user.com")
                .build();

        owner = User.builder()
                .id(2)
                .name("owner")
                .email("owner@user.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .author(user)
                .description("description")
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1)
                .ownerId(owner.getId())
                .name("item")
                .description("description")
                .available(true)
                .request(itemRequest)
                .build();

        userService = mock(UserServiceImplementation.class);
        itemService = mock(ItemServiceImplementation.class);
        itemRequestRepository = mock(ItemRequestDbRepository.class);
        itemRequestService = new ItemRequestServiceImplementation(
                userService, itemService, itemRequestRepository
        );

    }

    @Test
    @DisplayName("Тест метода findRequestById. Возвращает ItemRequest при валидных данных")
    void testFindRequestById_whenDataIsValid_thenReturnItemRequest() {
        when(userService.findById(anyInt())).thenReturn(UserMapper.userToDto(user));
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.ofNullable(itemRequest));
        ItemRequest savedRequest = itemRequestService.findRequestById(itemRequest.getId(), user.getId());
        Assertions.assertEquals(itemRequest.getAuthor(), savedRequest.getAuthor());
    }

    @Test
    @DisplayName("Тест метода findRequestById. Возвращает NotFoundException, когда user не найден")
    void testFindRequestById_whenUserNotFound_thenReturnNotFoundException() {
        when(userService.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findRequestById(itemRequest.getId(), user.getId())
        );
    }

    @Test
    @DisplayName("Тест метода findRequestById. Возвращает NotFoundException, когда itemRequest не найден")
    void testFindRequestById_whenItemRequestNotFound_thenReturnNotFoundException() {
        when(userService.findById(anyInt())).thenReturn(UserMapper.userToDto(user));
        when(itemRequestRepository.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findRequestById(itemRequest.getId(), user.getId())
        );
    }

    @Test
    @DisplayName("Тест метода findById. Возвращает ItemRequest при валидных данных")
    void testFindById_whenDataIsValid_thenReturnItemResponseDto() {
        when(userService.findById(anyInt())).thenReturn(UserMapper.userToDto(user));
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.ofNullable(itemRequest));
        ItemResponseDto savedRequest = itemRequestService.findById(itemRequest.getId(), user.getId());
        Assertions.assertEquals(itemRequest.getCreated(), savedRequest.getCreated());
    }

    @Test
    @DisplayName("Тест метода findById. Возвращает NotFoundException, когда user не найден")
    void testFindById_whenUserNotFound_thenReturnNotFoundException() {
        when(userService.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findById(itemRequest.getId(), user.getId())
        );
    }

    @Test
    @DisplayName("Тест метода findById. Возвращает NotFoundException, когда itemRequest не найден")
    void testFindById_whenItemRequestNotFound_thenReturnNotFoundException() {
        when(userService.findById(anyInt())).thenReturn(UserMapper.userToDto(user));
        when(itemRequestRepository.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findById(itemRequest.getId(), user.getId())
        );
    }

    @Test
    @DisplayName("Тест метода findAllByAuthor. Возвращает список ItemResponseDto по автору")
    void testFindAllByAuthor_whenInvoked_thenReturnListOfItemResponseDto() {
        ItemDto itemDto = ItemMapper.itemToDto(item);
        when(userService.findById(anyInt())).thenReturn(UserMapper.userToDto(user));
        when(itemRequestRepository.findAllByAuthorIdOrderByIdDesc(anyInt())).thenReturn(List.of(itemRequest));
        List<ItemResponseDto> response = itemRequestService.findAllByAuthor(user.getId());
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(itemRequest.getCreated(), response.get(0).getCreated());
    }

    @Test
    @DisplayName("Тест метода findAllByAuthor. Возвращает NotFoundException, когда user не найден")
    void testFindAllByAuthor_whenUserNotFound_thenReturnNotFoundException() {
        when(userService.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findAllByAuthor(user.getId())
        );
    }

    @Test
    @DisplayName("Тест метода findAll. Возвращает список всех ItemResponseDto, кроме авторских")
    void testFindAll_whenInvoked_thenReturnListOfItemResponseDto() {
        when(itemRequestRepository.findAllByAuthorIdNot(user.getId(), Pageable.ofSize(1))).thenReturn(Collections.emptyList());
        List<ItemResponseDto> response = itemRequestService.findAll(user.getId(), Pageable.ofSize(1));
        Assertions.assertEquals(0, response.size());
    }

    @Test
    @DisplayName("Тест метода findRequestById. С выбросом наружу NotFoundException")
    void testFindRequestById_whenItemRequestIsNotFound_thenThrowsNotFoundException() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findRequestById(itemRequest.getId(), user.getId())
        );
    }

    @Test
    @DisplayName("Тест метода findById. С выбросом наружу NotFoundException")
    void testFindById_whenItemRequestIsNotFound_thenThrowsNotFoundException() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.findById(itemRequest.getId(), user.getId())
        );
    }

    @Test
    @DisplayName("Тест метода save. Возвращает ItemRequestDto при валидных даннных")
    void testSave_whenDataIsValid_thenReturnItemResponseDto() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                user.getId(), itemRequest.getDescription(), itemRequest.getCreated()
        );
        when(userService.findById(anyInt())).thenReturn(UserMapper.userToDto(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        ItemResponseDto responseDto = itemRequestService.save(itemRequestDto);
    }
}
