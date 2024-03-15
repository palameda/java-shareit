package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentDbRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByItemIdOrderById(Integer itemId);
}
