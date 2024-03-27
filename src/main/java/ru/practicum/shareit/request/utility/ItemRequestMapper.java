package ru.practicum.shareit.request.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Утилитарный класс ItemRequestMapper предназначен для конвертации объектов, полученных в запросе и хранящихся в репозитории,
 * в объект для передачи данных и обратно.
 * @see ItemRequest
 * @see ItemRequestDto
 * @see ItemResponseDto
 */
@UtilityClass
public class ItemRequestMapper {

    /**
     * Статический метод requestDtoToItemRequest конвертирует объект, переданный в запросе,
     * в объект класса {@link ItemRequest}
     * @param requestDto объект класса {@link ItemRequestDto}, переданный в запросе
     * @param author объект класса {@link User}
     * @return объект класса {@link ItemRequest}
     */
    public static ItemRequest requestDtoToItemRequest(ItemRequestDto requestDto, User author) {
        return ItemRequest.builder()
                .author(author)
                .description(requestDto.getDescription())
                .created(requestDto.getCreated())
                .build();
    }

    /**
     * Статический метод itemRequestToResponseDto конвертирует объект класса {@link ItemRequest}
     * в объект-ответ класса {@link ItemResponseDto}
     * @param itemRequest - объект класса {@link ItemRequest}
     * @param items список запрошенных вещей
     * @return объект класса {@link ItemRequestDto}
     */
    public static ItemResponseDto itemRequestToResponseDto(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }
}
