package ru.practicum.shareit.comment.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

/**
 * Интерфейс CommentDbRepository содержит сигнатуры методов для работы с сущностью {@link Comment}.
 * Расширяет {@link JpaRepository}, который реализует основные CRUD-операции.
 */
public interface CommentDbRepository extends JpaRepository<Comment, Integer> {

    /**
     * Метод позволяет найти все комментарии, которые оставили для конкретной вещи
     * @param itemId - идентификатор вещи
     * @return список оставленных для вещи комментариев, преобразованных в {@link Comment}
     */
    List<Comment> findAllByItemIdOrderById(Integer itemId);
}
