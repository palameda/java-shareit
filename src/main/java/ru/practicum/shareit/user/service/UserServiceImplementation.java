package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;
import ru.practicum.shareit.user.utility.UserMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс UserServiceImplementation реализует методы интерфейса {@link UserService}.
 * Описывает логику работы приложения c сущностью {@link User}.
 * @see UserDbRepository
 * @see UserMapper
 * @see UserDto
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserDbRepository repository;

    @Override
    public List<UserDto> findAll() {
        log.info("Сервис: обработка запроса на получение списка всех пользователей");
        return repository.findAll().stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Integer userId) {
        log.info("Сервис: обработка запроса на получение пользователя по id {}", userId);
        return UserMapper.userToDto(repository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден")
        ));
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        log.info("Сервис: обработка запроса на сохранение нового пользователя {}", userDto.toString());
        try {
            return UserMapper.userToDto(
                    repository.save(UserMapper.dtoToUser(userDto))
            );
        } catch (DataIntegrityViolationException exception) {
            throw  new DuplicateDataException("Email адрес " + userDto.getEmail() + " уже зарегистрирован");
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Сервис: обработка запроса на обновление пользователя с id {}", userDto.getId());
        User storedUser = repository.findById(userDto.getId()).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userDto.getId() + " не найден")
        );
        if (userDto.getEmail() != null) {
            storedUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            storedUser.setName(userDto.getName());
        }
        try {
            return UserMapper.userToDto(
                    repository.save(storedUser)
            );
        } catch (DataIntegrityViolationException exception) {
            throw  new DuplicateDataException("Email адрес " + userDto.getEmail() + " уже зарегистрирован");
        }
    }

    @Transactional
    @Override
    public void deleteUser(Integer userId) {
        log.info("Сервис: обработка запроса на удаление пользователя с id {}", userId);
        repository.deleteById(userId);
    }
}
