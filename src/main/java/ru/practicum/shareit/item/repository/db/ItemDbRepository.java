package ru.practicum.shareit.item.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDbRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerId(Integer ownerId);

    @Query(value = "select it.id, it.name, it.description, it.available, it.owner_id as owner from items as it " +
            "where ((it.name ilike %?1%) or (it.description ilike %?1%)) and it.available", nativeQuery = true)
    List<Item> findByNameOrDescriptionAndAvailable(String query);
}
