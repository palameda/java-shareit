package ru.practicum.shareit.item.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDbRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerId(Integer ownerId);

    @Query(value = "select it from Item as it " +
        "where (UPPER(it.name) like UPPER(concat('%', ?1, '%')) " +
        "or UPPER(it.description) like UPPER(concat('%', ?1, '%'))) and it.available = true")
    List<Item> findByNameOrDescriptionAndAvailable(String query);
}
