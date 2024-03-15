package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс ItemRepository содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой хранилища для сущности {@link Item}
 */
@Deprecated
public interface ItemRepository {
    /**
     * Метод getAllItems возвращает список вещей, зарегистрированных на пользователя по userId
     * @param  userId идентификатор владельца
     * @return список всех вещей владельца
     */
    List<Item> getAllItems(Integer userId);

    /**
     * Метод getById возвращает данные о зарегистрированной вещи по переданному идентификатору.
     * Если в хранилище нет данных, удовлетворяющих запросу,
     * то возникает исключение {@link NotFoundException}
     * @param itemId идентификатор вещи
     * @return объект класса Item
     */
    Item getById(Integer itemId);

    /**
     * Метод saveItem добавляет в хранилище данные о новой вещи
     * @param item объект класса Item
     * @return добавленный объект класса Item
     */
    Item saveItem(Item item);

    /**
     * Метод updateItem обновляет данные о вещи в хранилище
     * @param item объект класса Item
     * @return обновлённый объект класса Item
     */
    Item updateItem(Item item);

    /**
     * Метод deleteItem удаляет данные о вещи в хранилище по идентификатору
     * @param itemId идентификатор вещи
     */
    void deleteItem(Integer itemId);

    /**
     * Метод seekItem возвращает список вещей, найденных по запросу searchQuery
     * @param searchQuery поисковый запрос, содержащий имя или описание вещи
     * @return список вещей, которые были найдены по поисковому запросу
     */
    List<Item> seekItem(String searchQuery);
}
