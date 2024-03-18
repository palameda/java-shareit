package ru.practicum.shareit.item.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс ItemDbRepository содержит сигнатуры методов для работы с сущностью {@link Item}.
 * Расширяет {@link JpaRepository}, который реализует основные CRUD-операции.
 */
public interface ItemDbRepository extends JpaRepository<Item, Integer> {

    /**
     * Метод позволяет владельцу найти все вещи по своему id
     * @param ownerId идентификатор арендодатора
     * @return список всех вещей владельца, преобразованных в {@link Item}
     */
    List<Item> findAllByOwnerId(Integer ownerId);


    /**
     * Метод позволяет найти все вещи, доступные к бронированию, по имени или описанию
     * @param query запрос к базе данных
     * @return список вещей, доступных к бронированию и преобразованных в {@link Item}
     */
    @Query(value = "select it from Item as it " +
        "where (UPPER(it.name) like UPPER(concat('%', ?1, '%')) " +
        "or UPPER(it.description) like UPPER(concat('%', ?1, '%'))) and it.available = true")
    List<Item> findByNameOrDescriptionAndAvailable(String query);
}
