package ru.practicum.shareit.item.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/**
 * <p>Утилитарный класс ItemMapper предназначен для конвертации объектов, хранящихся в репозитории,
 * в объекты для передачи данных и обратно.</p>
 * @see Item
 * @see ItemDto
 */

@UtilityClass
public class ItemMapper {
    /**
     * Статичный метод dtoToItem конвертирует объект itemDto в объект класса Item
     * @param  itemDto dto объект, содержащий данные о вещи
     * @return объект класса Item, полученный в результате преобразования itemDto
     */
    public static Item dtoToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    /**
     * Статичный метод itemToDto конвертирует объект item в объект класса ItemDto
     * @param item объект, содержащий данные о вещи
     * @return объект класса ItemDto, полученный в результате преобразования item
     */
    public static ItemDto itemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}
