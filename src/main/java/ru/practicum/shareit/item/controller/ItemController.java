package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс ItemController - контроллер, который обрабатывает GET, POST, PATCH и DELETE-методы запросов по эндпоинту /items.
 * Взаимодествует со слоем сервиса с помощью внедрённой зависимости {@link ItemService}.
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    /**
     * Метод findAll обрабатывает GET-метод запроса к эндпоинту /items,
     * обращается к методу {@link ItemService#findAll(Integer)}.
     * @param userId идентификатор владельца вещей
     * @return список всех вещей пользователя, преобразованных в {@link ItemDto}.
     */
    @GetMapping
    public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: GET-запрос по эндпоинту /items от пользователя с id {}", userId);
        return itemService.findAll(userId);
    }

    /**
     * Метод findById обрабатывает GET-метод запроса к эндпоинту /items/id,
     * обращается к методу {@link ItemService#findById(Integer)}.
     * @param itemId идентификатор вещи.
     * @return объект класса {@link Item}, преобразованный в {@link ItemDto}.
     */
    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable("id") Integer itemId) {
        log.info("Контроллер: GET-запрос по эндпоинту /items/{}", itemId);
        return itemService.findById(itemId);
    }

    /**
     * Метод saveItem обрабатывает POST-метод запроса к эндпоинту /items,
     * обращается к методу {@link ItemService#saveItem(ItemDto, Integer)}.
     * <strong>Вещь может добавить только её владелец.</strong>
     * @param itemDto объект класса {@link ItemDto}.
     * @param userId идентификатор владельца вещи
     * @return сохранённый объект класса {@link Item}, преобразованный в {@link ItemDto}.
     */
    @PostMapping
    public ItemDto saveItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: POST-запрос по эндпоинту /items пользователем с id {}", userId);
        return itemService.saveItem(itemDto, userId);
    }

    /**
     * Метод updateItem обрабатывает PATCH-метод запроса к эндпоинту /items/id,
     * обращается к методу {@link ItemService#updateItem(ItemDto, Integer)}.
     * <strong>Только владелец вещи может выполнять редактирование.</strong>
     * @param itemDto объект класса {@link ItemDto}.
     * @param userId идентификатор владельца вещи.
     * @param itemId идентификатор вещи, данные которой нужно отредактировать.
     * @return изменённый объект класса {@link Item}, преобразованный в {@link ItemDto}.
     */
    @PatchMapping("{id}")
    public ItemDto updateItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable("id") Integer itemId) {
        log.info("Контроллер: PATCH-запрос по эндпоинту /items/{} пользователем с id {}", itemId, userId);
        itemDto.setId(itemId);
        return itemService.updateItem(itemDto, userId);
    }

    /**
     * Метод deleteItem обрабатывает DELETE-метод запроса к эндпоинту /items/id,
     * обращается к методу {@link ItemService#deleteItem(Integer, Integer)}.
     * <strong>Только владелец вещи может её удалить.</strong>
     * @param itemId идентификатор вещи, данные которой нужно удалить.
     * @param userId идентификатор владельца вещи.
     */
    @DeleteMapping("{id}")
    public void deleteItem(@PathVariable("id") Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: DELETE-запрос по эндпоинту /items/{} пользователем с id {}", itemId, userId);
        itemService.deleteItem(itemId, userId);
    }

    /**
     * Метод findItems обрабатывает GET-метод запроса к энпоинту /items/search,
     * обращается к методу {@link ItemService#seekItem(String)}.
     * @param text поисковый запрос.
     * @return список найденных вещей, которые можно арендовать.
     */
    @GetMapping("/search")
    public List<ItemDto> findItems(@RequestParam String text) {
        log.info("Контроллер: GET-запрос по эндпоинту /items/search со строкой {}", text);
        return itemService.seekItem(text);
    }
}
