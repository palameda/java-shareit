package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Интерфейс ItemRequestService содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой сервиса для сущности {@link ItemRequest}.
 */
public interface ItemRequestService {

    /**
     * Метод для получения запроса на создание вещи по его идентификатору
     * @param itemRequestId идентификатор запроса на создание вещи
     * @param userId идентификатор пользователя, осуществляющего запрос
     * @return объект класса {@link ItemRequest}
     */
    ItemRequest findRequestById(Integer itemRequestId, Integer userId);

    /**
     * Метод для получения запроса на создание вещи по его идентификатору
     * @param itemRequestId идентификатор запроса на создание вещи
     * @param userId идентификатор пользователя, осуществляющего запрос
     * @return объект класса {@link ItemResponseDto}
     */
    ItemResponseDto findById(Integer itemRequestId, Integer userId);

    /**
     * Метод для получения всех запросов для их автора
     * @param userId идентификатор автора запроса
     * @return список объектов класса {@link ItemResponseDto}
     */
    List<ItemResponseDto> findAllByAuthor(Integer userId);

    /**
     * Метод для получения всех запросов пользователя
     * @param userId идентификатор пользователя
     * @param pageable критерий отображения результатов
     * @return список объектов класса {@link ItemResponseDto}
     */
    List<ItemResponseDto> findAll(Integer userId, Pageable pageable);

    /**
     * Метод для сохранения запроса на добавление вещи
     * @param requestDto объект класса {@link ItemRequestDto}
     * @return объект класса {@link ItemResponseDto}
     */
    ItemResponseDto save(ItemRequestDto requestDto);
}
