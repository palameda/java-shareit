package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс UserRepository содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой хранилища для сущности User
 */

public interface UserRepository {
    /**
     * Метод getAllUsers возвращает список зарегистрированных пользователей
     * @return List
     */
    List<User> getAllUsers();

    /**
     * Метод getById возвращает данные зарегистрированного пользователя по переданному идентификатору
     * @param id
     * @return User
     */
    User getById(Integer id);


    /**
     * Метод addUser добавляет в хранилище нового пользователя
     * @param user
     * @return User
     */
    User addUser(User user);

    /**
     * Метод updateUser обновляет данные зарегистрированного пользователя
     * @param user
     * @return User
     */
    User updateUser(User user);


    /**
     * Метод deleteUser удаляет из хранилища данные зарегистрированного пользователя по переданному идентификатору
     * @param id
     */
    void deleteUser(Integer id);

}
