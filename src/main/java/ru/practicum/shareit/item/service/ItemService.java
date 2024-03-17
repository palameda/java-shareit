package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.DenialOfAccessException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс ItemService содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой сервиса для сущности {@link Item}.
 */
public interface ItemService {
    /**
     * Сервисный метод findAll возвращает список вещей, зарегистрированных на пользователя по userId.
     * @param  userId идентификатор владельца
     * @return список всех вещей владельца, преобразованных в ItemDto
     */
    List<ItemDto> findAll(Integer userId);

    /**
     * Сервисный метод findById возвращает данные о зарегистрированной вещи по переданному идентификатору.
     * @param itemId идентификатор вещи
     * @param userId идентификатор пользователя
     * @return преобразованный в ItemDto объект класса Item
     */
    ItemDto findById(Integer itemId, Integer userId);

    /**
     * Сервисный метод saveItem отправляет запрос к хранилищу на сохранение данных о новой вещи её владельцем.
     * @param itemDto объект класса ItemDto
     * @param userId идентификатор владельца вещи
     * @return преобразованный в ItemDto объект класса Item
     */
    ItemDto saveItem(ItemDto itemDto, Integer userId);

    /**
     * Сервисный метод updateItem отправляет запрос к хранилищу на обновление данных о вещи её владельцем
     * @param itemDto объект класса ItemDto
     * @param userId идентификатор владельца вещи
     * @return преобразованный в ItemDto объект класса Item
     */
    ItemDto updateItem(ItemDto itemDto, Integer userId);

    /**
     * Сервисный метод deleteItem отправляет запрос к хранилищу на удаление данных о вещи её владельцем.
     * При попытке удаления вещи пользователем, не являющимся её владельцем,
     * возникает исключение {@link DenialOfAccessException}
     * @param itemId идентификатор вещи
     * @param userId идентификатор владельца вещи
     */
    void deleteItem(Integer itemId, Integer userId);

    /**
     * Сервисный метод seekItem возвращает список вещей, найденных по запросу searchQuery.
     * @param searchQuery поисковый запрос, содержащий имя или описание вещи
     * @return список вещей, преобразованных в ItemDto, которые были найдены по поисковому запросу
     */
    List<ItemDto> seekItem(String searchQuery);

    /**
     * Сервисный метод addComment возвращает комментрий, который оставляет пользователь.
     * @param comment объекта класса {@link Comment}
     * @return комментарий пользователя, преобразованный в объект класса ResponseComment
     */
    ResponseComment addComment(RequestComment comment);
}
