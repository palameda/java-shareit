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
    /**
     * Метод возвращает список всех запросов по автору
     * @param userId идентификатор автора запроса
     * @return список объектов {@link ItemRequest}
     */
    List<ItemRequest> findAllByAuthorIdOrderByIdDesc(Integer userId);

    /**
     * Метод возвращает список всех запросом, кроме запросов автора
     * @param userId идентификатор автора запроса
     * @param pageable количество записей на странице {@link Pageable}
     * @return список объектов {@link ItemRequest}
     */
    List<ItemRequest> findAllByAuthorIdNot(Integer userId, Pageable pageable);
}
