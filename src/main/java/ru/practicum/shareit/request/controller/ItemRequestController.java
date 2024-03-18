package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Класс ItemRequestController - контроллер, который обрабатывает GET и POST-методы запросов по эндпоинту /requests.
 * Взаимодествует со слоем сервиса с помощью внедрённой зависимости {@link ItemRequestService}.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    /**
     * Обрабатывает GET-метод запроса к эндпоинту /requests,
     * обращается к методу {@link ItemRequestService#findAllByAuthor(Integer)}
     * @param userId идентификатор автора запросов
     * @return список всех запросов автора, преобразованных в {@link ItemResponseDto}
     */
    @GetMapping()
    public List<ItemResponseDto> findRequestsByAuthor(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: GET-запрос по эндпоинту /requests от пользователя с id {}", userId);
        return itemRequestService.findAllByAuthor(userId);
    }

    /**
     * Обрабатывает GET-метод запроса к эндпоинту /requests/id,
     * обращается к методу {@link ItemRequestService#findById(Integer, Integer)}
     * @param id идентификатор запроса
     * @param userId идентификатор пользователя
     * @return запрос на добавление вещи, преобразованный в {@link ItemResponseDto}
     */
    @GetMapping("/{id}")
    public ItemResponseDto findRequestById(@PathVariable("id") Integer id,
                                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Контроллер: GET-запрос по эндпоинту /requests/{} от пользователя с id {}", id, userId);
        return itemRequestService.findById(id, userId);
    }

    /**
     * Обрабатывает GET-метод запроса к эндпоинту /requests/all,
     * обращается к методу {@link ItemRequestService#findAll(Integer, Pageable)}
     * @param userId идентификатор пользователя
     * @param from номер первой записи
     * @param size желаемое количество элементов для отображения
     * @return список всех запросов, преобразованных в {@link ItemResponseDto}
     */
    @GetMapping("/all")
    public List<ItemResponseDto> findAllRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(defaultValue = "1") @Min(1) int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").descending());
        log.info("Контроллер: GET-запрос по эндпоинту /requests/all от пользователя с id={}, from={}, size={}",
                userId, from, size);
        return itemRequestService.findAll(userId, page);
    }

    /**
     * Обрабатывает POST-метод запроса к эндпоинту /requests,
     * обращается к методу {@link ItemRequestService#save(ItemRequestDto)}
     * @param requestDto запрос на добавление вещи
     * @param userId идентификатор автора запроса
     * @return запрос на добавление вещи, преобразованный в {@link ItemResponseDto}
     */
    @PostMapping()
    public ItemResponseDto save(@Valid @RequestBody ItemRequestDto requestDto,
                                @RequestHeader("X-Sharer-User-Id") Integer userId) {
        requestDto.setUserId(userId);
        log.info("Контроллер: POST-запрос по эндпоинту /requests от пользователя с id {}", userId);
        return itemRequestService.save(requestDto);
    }
}
