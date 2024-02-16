package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: GET-запрос по эндпоинту /items от пользователя с id {}", userId);
        return itemService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable("id") Integer itemId) {
        log.info("Контроллер: GET-запрос по эндпоинту /items/{}", itemId);
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto saveItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: POST-запрос по эндпоинту /items пользователем с id {}", userId);
        return itemService.saveItem(itemDto, userId);
    }

    @PatchMapping("{id}")
    public ItemDto updateItem(
            @Valid @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable("id") Integer itemId) {
        log.info("Контроллер: PATCH-запрос по эндпоинту /items/{} пользователем с id {}", itemId, userId);
        itemDto.setId(itemId);
        return itemService.updateItem(itemDto, userId);
    }

    @DeleteMapping("{id}")
    public void deleteItem(@PathVariable("id") Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: DELETE-запрос по эндпоинту /items/{} пользователем с id {}", itemId, userId);
        itemService.deleteItem(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItems(@RequestParam String text) {
        log.info("Контроллер: GET-запрос по эндпоинту /items/search со строкой {}", text);
        return itemService.seekItem(text);
    }
}
