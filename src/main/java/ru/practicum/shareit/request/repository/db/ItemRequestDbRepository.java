package ru.practicum.shareit.request.repository.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Интерфейс RequestDbRepository содержит сигнатуры методов для работы с сущностью {@link ItemRequest}.
 * Расширяет {@link PagingAndSortingRepository}, предоставляющий методы для получения сущностей
 * с использованием абстракции пагинации и сортировки
 */
public interface ItemRequestDbRepository extends PagingAndSortingRepository<ItemRequest, Integer> {
    List<ItemRequest> findAllByAuthorIdOrderByIdDesc(Integer userId);

    List<ItemRequest> findAllByAuthorIdNot(Integer userId, Pageable pageable);
}
