package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс UserController - контроллер, который обрабатывает GET, POST, PATCH и DELETE-методы запросов по эндпоинту /users.
 * Взаимодествует со слоем сервиса с помощью внедрённой зависимости {@link UserService}.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    /**
     * Метод findAll обрабатывает GET-метод запроса по эндпоинту /users,
     * обращается к методу {@link UserService#findAll()}.
     * @return список всех зарегистрированных пользователей, преобразованных в userDto.
     */
    @GetMapping
    public List<UserDto> findAll() {
        log.info("Контроллер: GET-запрос по эндпоинту /users");
        return userService.findAll();
    }

    /**
     * Метод findById обрабатывает GET-метод запроса по эндпоинту /users/id,
     * обращается к методу {@link UserService#findById(Integer)}.
     * @param userId идентификатор пользователя.
     * @return объект класса {@link User}, преобразованный в {@link UserDto}.
     */
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") Integer userId) {
        log.info("Контроллер: GET-запрос по эндпоинту /users/{}", userId);
        return userService.findById(userId);
    }

    /**
     * Метод saveUser обрабатывает POST-метод запроса по эндпоинту /users,
     * обращается к методу {@link UserService#saveUser(UserDto)}.
     * @param userDto объект класса {@link UserDto}.
     * @return сохранённый объект класса {@link User}, преобразованный в {@link UserDto}.
     */
    @PostMapping
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Контроллер: POST-запрос по эндпоинту /users");
        return userService.saveUser(userDto);
    }

    /**
     * Метод updateUser обрабатывает PATCH-метод запроса по эндпоинту /users/id,
     * обращается к методу {@link UserService#updateUser(UserDto)}.
     * @param userDto объект класса {@link UserDto}.
     * @param userId идентификатор пользователя, которого нужно отредактировать.
     * @return изменённый объект класса {@link User}, преобразованный в {@link UserDto}.
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable("id") Integer userId) {
        log.info("Контроллер: PATCH-запрос по эндпоинту /users/{}", userId);
        userDto.setId(userId);
        return userService.updateUser(userDto);
    }

    /**
     * Метод deleteUser обрабатывает DELETE-метод запроса по эндпоинту /users/id,
     * обращается к методу {@link UserService#deleteUser(Integer)}.
     * @param userId идентификатор пользователя, которого нужно удалить.
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer userId) {
        log.info("Контроллер: DELETE-запрос по эндпоинту /users/{}", userId);
        userService.deleteUser(userId);
    }
}
