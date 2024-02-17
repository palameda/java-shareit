package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.utility.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс UserServiceImplementation реализует методы интерфейса {@link UserService}.
 * Описывает логику работы приложения c сущностью {@link User}.
 * @see UserRepository
 * @see UserMapper
 * @see UserDto
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> findAll() {
        log.info("Сервис: обработка запроса на получение списка всех пользователей");
        return repository.getAllUsers().stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Integer userId) {
        log.info("Сервис: обработка запроса на получение пользователя по id {}", userId);
        return UserMapper.userToDto(repository.getById(userId));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.info("Сервис: обработка запроса на сохранение нового пользователя {}", userDto.toString());
        return UserMapper.userToDto(
                repository.saveUser(UserMapper.dtoToUser(userDto))
        );
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Сервис: обработка запроса на обновление пользователя с id {}", userDto.getId());
        repository.getById(userDto.getId());
        return UserMapper.userToDto(
                repository.updateUser(UserMapper.dtoToUser(userDto))
        );
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("Сервис: обработка запроса на удаление пользователя с id {}", userId);
        repository.deleteUser(userId);
    }
}
