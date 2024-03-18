package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс UserService содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой сервиса для сущности {@link User}
 * @see UserDto
 */
public interface UserService {
    /**
     * Сервисный метод findAll возвращает список зарегистрированных пользователей
     * @return список пользователей, преобразованных в UserDto
     */
    List<UserDto> findAll();

    /**
     * Сервисный метод findById возвращает данные зарегистрированного пользователя по переданному идентификатору
     * @param userId идентификатор пользователя
     * @return объект класса User, преобразованный в UserDto
     */
    UserDto findById(Integer userId);

    /**
     * Сервисный метод saveUser оправляет хранилищу запрос на сохранение данных о новом пользователе
     * @param userDto данные нового пользователя
     * @return сохранённый объект класса User, преобразованный в UserDto
     */
    UserDto saveUser(UserDto userDto);

    /**
     * Сервисный метод updateUser оправляет хранилищу запрос на обновление данных о пользователе
     * @param userDto данные для обновления пользователя
     * @return обновлённый объект класса User, преобразованный в UserDto
     */
    UserDto updateUser(UserDto userDto);

    /**
     * Сервисный метод deleteUser оправляет хранилищу запрос на удаление данных о пользователе по идентификатору
     * @param userId идентификатор пользователя
     */
    void deleteUser(Integer userId);
}
