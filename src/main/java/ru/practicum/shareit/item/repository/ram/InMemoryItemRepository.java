package ru.practicum.shareit.item.repository.ram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс InMemoryItemRepository реализует методы интерфейса {@link ItemRepository}.
 * Описывает реализацию фунциональности для хранения данных о вещах в оперативной памяти.
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final List<Item> items;
    private Integer id = 1;

    @Override
    public List<Item> getAllItems(Integer userId) {
        log.info("Хранилище: получение списка всех вещей, зарегистрированных на пользователя с id {}", userId);
        return items.stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(Integer itemId) {
        log.info("Хранилище: получение данных о вещи по id {}", itemId);
        Optional<Item> item = items.stream()
                .filter(i -> Objects.equals(i.getId(), itemId))
                .findFirst();
        return item.orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
    }

    @Override
    public Item saveItem(Item item) {
        log.info("Хранилище: добавление данных о новой вещи {}", item.getName());
        item.setId(id);
        id++;
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        log.info("Хранилище: обновление данных для вещи с id {}", item.getId());
        Item updatedItem = getById(item.getId());
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        return updatedItem;
    }

    @Override
    public void deleteItem(Integer itemId) {
        log.info("Хранилище: удаление вещи с id {}", itemId);
        items.remove(getById(itemId));
    }

    @Override
    public List<Item> seekItem(String searchQuery) {
        log.info("Хранилище: поиск вещи по запросу {}", searchQuery);
        return items.stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(searchQuery.toLowerCase())))
                .collect(Collectors.toList());
    }
}
