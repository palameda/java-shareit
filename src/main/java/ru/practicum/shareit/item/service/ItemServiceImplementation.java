package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DenialOfAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.utility.ItemMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс ItemServiceImplementation реализует методы интерфейса {@link ItemService}.
 * Описывает логику работы приложения c сущностью {@link Item}.
 * @see ItemRepository
 * @see UserRepository
 * @see ItemMapper
 * @see ItemDto
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> findAll(Integer userId) {
        log.info("Сервис: обработка запроска на получение списка всех вещей владельца с id {}", userId);
        return itemRepository.getAllItems(userId).stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Integer itemId) {
        log.info("Сервис: обработка запроса на получение вещи с id {}", itemId);
        return ItemMapper.itemToDto(itemRepository.getById(itemId));
    }

    @Override
    public ItemDto saveItem(ItemDto itemDto, Integer userId) {
        log.info("Сервис: обработка запроса на сохранение вещи {} её владельцем с id {}", itemDto.getName(), userId);
        userRepository.getById(userId);
        Item savedItem = ItemMapper.dtoToItem(itemDto);
        savedItem.setOwnerId(userId);
        return ItemMapper.itemToDto(
                itemRepository.saveItem(savedItem)
        );
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Integer userId) {
        log.info("Сервис: обработка запроса на изменение данных вещи {} пользователем с id {}", itemDto.getName(), userId);
        userRepository.getById(userId);
        checkPossession(itemDto, userId);
        return ItemMapper.itemToDto(
                itemRepository.updateItem(ItemMapper.dtoToItem(itemDto))
        );
    }

    @Override
    public void deleteItem(Integer itemId, Integer userId) {
        log.info("Сервис: обработка запроса на удаление вещи {} пользователем с id {}", itemId, userId);
        userRepository.getById(userId);
        ItemDto checkItem = ItemMapper.itemToDto(itemRepository.getById(itemId));
        checkPossession(checkItem, userId);
        itemRepository.deleteItem(itemId);
    }

    @Override
    public List<ItemDto> seekItem(String searchQuery) {
        log.info("Сервис: обработка поискового запроса {}", searchQuery);
        return itemRepository.seekItem(searchQuery).stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
    }

    /**
     * Приватный метод checkPossession проверяет является ли пользователь владельцем вещи.
     * Если пользователь не является владельцем вещи, то возникает исключение {@link DenialOfAccessException}
     * @param itemDto данные вещи для проверки
     * @param ownerId идентификатор пользователя для проверки
     */
    private void checkPossession(ItemDto itemDto, Integer ownerId) {
        log.info("Сервис: проверка принадлежности вещи {} пользователю с id {}", itemDto.getName(), ownerId);
        Item savedItem = itemRepository.getById(itemDto.getId());
        if (!Objects.equals(savedItem.getOwnerId(), ownerId)) {
            throw new DenialOfAccessException(
                    "Отказ в доступе. Пользователь с id " + ownerId + " не является владельцем вещи " + itemDto.getName()
            );
        }
    }
}
