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
 * Класс InMemoryUserRepository реализует методы интерфейса UserRepository.
 * Описывает реализацию фунциональности для хранения данных пользователей в оперативной памяти.
 */

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users;

    private Long id = 1L;

    @Override
    public List<User> getAllUsers() {
        log.info("Хранилище: получение списка всех зарегистрированных пользователей");
        return users;
    }

    @Override
    public User getById(Long id) {
        log.info("Хранилище: получение пользователя по id {}", id);
        Optional<User> user = users.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst();
        return user.orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    @Override
    public User addUser(User user) {
        log.info("Хранилище: добавление нового пользователя");
        checkEmail(user.getEmail());
        user.setId(id);
        id++;
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Хранилище: обновление пользователя с id {}", user.getId());
        checkEmail(user.getEmail());
        User updatedUser = users.get(Math.toIntExact(user.getId()));
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Хранилище: удаление пользователя с id {}", id);
        users.remove(getById(id));
    }

    /**
     * Приватный метод checkEmail выполняет проверку переданного в качестве параметра email.
     * Если email уже зарегистрирован в системе, то будет выброщено исключение DuplicateDataException.
     * @param email
     */
    private void checkEmail(String email) { // следует вынести проверку в слой сервиса
        log.info("Хранилище: проверка email {}", email);
        if (email == null) {
            return;
        }
        Optional<User> registeredEmail = users.stream()
                .filter(u -> Objects.equals(u.getEmail(), email))
                .findFirst();
        if (registeredEmail.isPresent()) {
            throw new DuplicateDataException("Email уже зарегистрирован в системе");
        }
    }
}
