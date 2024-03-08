package ru.practicum.shareit.user.repository.ram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс InMemoryUserRepository реализует методы интерфейса {@link UserRepository}.
 * Описывает реализацию фунциональности для хранения данных пользователей в оперативной памяти.
 */

@Slf4j
@Repository
@RequiredArgsConstructor
@Deprecated
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users;

    private Integer id = 1;

    @Override
    public List<User> getAllUsers() {
        log.info("Хранилище: получение списка всех зарегистрированных пользователей");
        return users;
    }

    @Override
    public User getById(Integer userId) {
        log.info("Хранилище: получение пользователя по id {}", userId);
        Optional<User> user = users.stream()
                .filter(u -> Objects.equals(u.getId(), userId))
                .findFirst();
        return user.orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    @Override
    public User saveUser(User user) {
        log.info("Хранилище: добавление нового пользователя");
        checkEmail(user.getEmail(), id);
        user.setId(id);
        id++;
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Хранилище: обновление пользователя с id {}", user.getId());
        checkEmail(user.getEmail(), user.getId());
        User updatedUser = getById(user.getId());
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("Хранилище: удаление пользователя с id {}", userId);
        users.remove(getById(userId));
    }

    /**
     * Приватный метод checkEmail выполняет проверку переданного в качестве параметра email.
     * Если email уже зарегистрирован в системе, то будет выброщено исключение {@link DuplicateDataException}.
     * @param email адрес электронной почты для проверки
     */
    private void checkEmail(String email, Integer userId) {
        log.info("Хранилище: проверка email {}", email);
        if (email == null) {
            return;
        }
        Optional<User> registeredEmail = users.stream()
                .filter(u -> Objects.equals(u.getEmail(), email) && !Objects.equals(u.getId(), userId))
                .findFirst();
        if (registeredEmail.isPresent()) {
            throw new DuplicateDataException("Email уже зарегистрирован в системе");
        }
    }
}
