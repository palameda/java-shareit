package ru.practicum.shareit.user.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

public interface UserDbRepository extends JpaRepository<User, Integer> {
}
