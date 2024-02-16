package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Контроллер: GET-запрос по эндпоинту /users");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") Integer userId) {
        log.info("Контроллер: GET-запрос по эндпоинту /users/{}", userId);
        return userService.findById(userId);
    }

    @PostMapping
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Контроллер: POST-запрос по эндпоинту /users");
        return userService.saveUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable("id") Integer userId) {
        log.info("Контроллер: PATCH-запрос по эндпоинту /users/{}", userId);
        userDto.setId(userId);
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer userId) {
        log.info("Контроллер: DELETE-запрос по эндпоинту /users/{}", userId);
        userService.deleteUser(userId);
    }
}
