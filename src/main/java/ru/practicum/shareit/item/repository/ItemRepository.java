package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс ItemRepository содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой хранилища для сущности Item
 */
public interface ItemRepository {
    /**
     * Метод getAllItems возвращает список вещей, зарегистрированных на пользователя по ownerId
     * @param ownerId
     * @return List
     */
    List<Item> getAllItems(Integer ownerId);

    /**
     * Метод getById возвращает данные о зарегистрированной вещи по переданному идентификатору
     * @param id
     * @return Item
     */
    Item getById(Integer id);

    /**
     * Метод addItem добавляет в хранилище данные о новой вещи
     * @param item
     * @return Item
     */
    Item addItem(Item item);

    /**
     * Метод updateItem обновляет данные о вещи в хранилище
     * @param item
     * @return Item
     */
    Item updateItem(Item item);

    /**
     * Метод deleteItem удаляет данные о вещи в хранилище
     * @param id
     */
    void deleteItem(Integer id);

    /**
     * Метод seekItem возвращает список вещей, найденных по запросу searchQuery
     * @param searchQuery
     * @return List
     */
    List<Item> seekItem(String searchQuery);
}
