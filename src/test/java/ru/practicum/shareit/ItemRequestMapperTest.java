package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.utility.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.utility.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapperTest {
    private User user = User.builder()
            .id(1)
            .name("user")
            .email("user@email.com")
            .build();

    private ItemRequest itemRequest = ItemRequest.builder()
            .id(1)
            .author(user)
            .description("description")
            .created(LocalDateTime.now())
            .build();

    private ItemRequestDto requestDto = new ItemRequestDto(user.getId(), "description", LocalDateTime.now());

    private Item item = Item.builder()
            .id(1)
            .ownerId(user.getId())
            .name("item")
            .description("description")
            .available(true)
            .request(itemRequest)
            .build();

    @Test
    @DisplayName("Проверка маппинга сущности ItemRequest запрос-модель")
    void requestDtoToItemRequest_whenReceivedItemRequestDto_thenReturnItemRequest() {
        ItemRequest receivedItem = ItemRequestMapper.requestDtoToItemRequest(requestDto, user);
        Assertions.assertEquals(receivedItem.getAuthor().toString(), user.toString());
    }

    @Test
    @DisplayName("Проверка маппинга сущности ItemRequest модель-ответ")
    void itemRequestToResponseDto_whenReceivedItemRequest_thenReturnItemResponseDto() {
        ItemDto itemDto = ItemMapper.itemToDto(item);
        ItemResponseDto responseDto = ItemRequestMapper.itemRequestToResponseDto(itemRequest, List.of(itemDto));
        Assertions.assertEquals(responseDto.getDescription(), item.getDescription());
    }
}
