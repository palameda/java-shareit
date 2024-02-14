package ru.practicum.shareit.item.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/**
 * <p>Утилитарный класс ItemMapper предназначен для конвертации объектов, хранящихся в репозитории,
 * в объекты для передачи данных и обратно.</p>
 */

@UtilityClass
public class ItemMapper {
    /**
     * Статичный метод DtoToItem конвертирует объект itemDto в объект класса Item
     * @param  itemDto
     * @return Item
     */
    public static Item DtoToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .ownerId(itemDto.getOwnerId())
                .available(itemDto.getAvailable())
                .build();
    }

    /**
     * Статичный метод ItemToDto конвертирует объект item в объект класса ItemDto
     * @param item
     * @return ItemDto
     */
    public static ItemDto ItemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getAvailable())
                .build();
    }
}
