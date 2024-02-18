package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

/**
 * Интерфейс UserRepository содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой хранилища для сущности {@link User}.
 */

public interface UserRepository {
    /**
     * Метод getAllUsers возвращает список зарегистрированных пользователей.
     * @return список пользователей
     */
    List<User> getAllUsers();

    /**
     * Метод getById возвращает данные зарегистрированного пользователя по переданному идентификатору.
     * Если пользователь не зарегистрирован, то возникает исключение {@link NotFoundException}.
     * @param userId идентификатор пользователя
     * @return извлечённый из хранилия объект класса User
     */
    User getById(Integer userId);

    /**
     * Метод saveUser сохраняет в хранилище данные нового пользователя
     * @param user данные для сохранения
     * @return сохранённый в хранилище объект класса User
     */
    User saveUser(User user);

    /**
     * Метод updateUser обновляет данные зарегистрированного пользователя.
     * @param user данные для обновления
     * @return обновлённый объект класса User
     */
    User updateUser(User user);

    /**
     * Метод deleteUser удаляет из хранилища данные зарегистрированного пользователя по переданному идентификатору.
     * @param userId
     */
    void deleteUser(Integer userId);
}
