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
 * Класс InMemoryItemRepository реализует методы интерфейса ItemRepository.
 * Описывает реализацию фунциональности для хранения данных о вещах в оперативной памяти.
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final List<Item> items;
    private Integer id = 1;

    @Override
    public List<Item> getAllItems(Integer ownerId) {
        log.info("Хранилище: получение списка всех вещей, зарегистрированных на пользователя с id {}", ownerId);
        return items.stream()
                .filter(item -> Objects.equals(item.getOwnerId(), ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(Integer id) {
        log.info("Хранилище: получение данных о вещи по id {}", id);
        Optional<Item> item = items.stream()
                .filter(i -> Objects.equals(i.getId(), id))
                .findAny();
        return item.orElseThrow(() -> new NotFoundException("Вещь с id " + id + " не найдена"));
    }

    @Override
    public Item addItem(Item item) {
        log.info("Хранилище: добавление данных о новой вещи");
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
    public void deleteItem(Integer id) {
        log.info("Хранилище: удаление вещи с id {}", id);
        items.remove(getById(id));
    }

    @Override
    public List<Item> seekItem(String searchQuery) {
        if (searchQuery == null) {
            return List.of();
        }
        return items.stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(searchQuery.toLowerCase())))
                .collect(Collectors.toList());
    }
}
