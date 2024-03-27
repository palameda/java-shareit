package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;
import ru.practicum.shareit.user.service.UserServiceImplementation;
import ru.practicum.shareit.user.utility.UserMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDbRepository userRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    private Integer userId;
    private User user;
    private User updateUser;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        userId = 1;
        user = User.builder()
                .id(1)
                .name("user")
                .email("user@user.com")
                .build();

        updateUser = User.builder()
                .id(1)
                .name("update")
                .email("update@user.com")
                .build();

        userDto = UserDto.builder()
                .name("user")
                .email("user@user.com")
                .build();
    }

    @Test
    @DisplayName("Тест метода findAll. Возвращает список пользователей")
    void findAll_whenInvoked_thenReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDto> actualUsers = userService.findAll();
        Assertions.assertFalse(actualUsers.isEmpty());
        Assertions.assertEquals(actualUsers.size(), 1);
        Assertions.assertEquals(actualUsers.get(0).getName(), user.getName());
    }

    @Test
    @DisplayName("Тест метода findById. Возвращает UserDto, когда пользователь найден")
    void findById_whenUserFound_thenReturnUser() {
        User expectedUser = User.builder().build();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));
        User actualUser = UserMapper.dtoToUser(userService.findById(userId));
        Assertions.assertEquals(expectedUser.toString(), actualUser.toString());
    }

    @Test
    @DisplayName("Тест метода findById. Выбрасывает NotFoundException, когда пользователь не найден")
    void findById_whenUserNotFound_thenNotFoundExceptionThrown() {
        String errorMessage = "Пользователь с id " + userId + " не найден";
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.findById(userId)
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("Тест метода saveUser. Возвращает пользователя, когда сохранение прошло успешно")
    void saveUser_whenUserSaved_thenReturnUserDto() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User actualUser = UserMapper.dtoToUser(userService.saveUser(userDto));
        verify(userRepository, times(1)).save(any(User.class));
        Assertions.assertEquals(actualUser.toString(), user.toString());
    }

    @Test
    @DisplayName("Тест метода saveUser. Выбрасывает исключение DuplicateDataException, когда email уже зарегистрирован")
    void saveUser_whenUserEmailAlreadyRegistered_thenThrowDuplicateDataException() {
        doThrow(DuplicateDataException.class).when(userRepository).save(user);
        Assertions.assertThrows(
                DuplicateDataException.class,
                () -> userRepository.save(user)
        );
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Тест метода updateUser. Возвращает пользователя, когда обновление прошло успешно")
    void updateUser_whenUserUpdated_thenReturnUserDto() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updateUser);
        User actualUser = UserMapper.dtoToUser(userService.updateUser(UserMapper.userToDto(updateUser)));
        verify(userRepository, times(1)).save(any(User.class));
        Assertions.assertEquals(actualUser.toString(), updateUser.toString());
    }

    @Test
    @DisplayName("Тест метода UpdateUser. Выбрасывает исключение DuplicateDataException, когда email уже зарегистрирован")
    void updateUser_whenUserEmailAlreadyRegistered_thenThrowDuplicateDataException() {
        doThrow(DuplicateDataException.class).when(userRepository).save(updateUser);
        Assertions.assertThrows(
                DuplicateDataException.class,
                () -> userRepository.save(updateUser)
        );
        verify(userRepository).save(updateUser);
    }

    @Test
    @DisplayName("Тест метода UpdateUser. Выбрасывает исключение DataIntegrityViolationException, когда email уже зарегистрирован")
    void updateUser_whenUserEmailAlreadyRegistered_thenThrowDataIntegrityException() {
        doThrow(DataIntegrityViolationException.class).when(userRepository).save(updateUser);
        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> userRepository.save(updateUser)
        );
        verify(userRepository).save(updateUser);
    }


    @Test
    @DisplayName("Тест метода DeleteUser. Удаляет пользователя")
    void deleteUser_whenInvoked_thenNotThrownAnyException() {
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Тест метода saveUser. Выбрасывает DuplicationDataException, т.к. email уже зарегистрирован")
    void testSaveUser_whenEmailAlreadyRegistered_thenDuplicationDataExceptionThrown() {
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertThrows(
                DuplicateDataException.class,
                () -> userService.saveUser(UserMapper.userToDto(user))
        );
    }

    @Test
    @DisplayName("Тест метода updateUser. Выбрасывает DuplicationDataException, т.к. email уже зарегистрирован")
    void testUpdateUser_whenEmailAlreadyRegistered_thenDuplicationDataExceptionThrown() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertThrows(
                DuplicateDataException.class,
                () -> userService.updateUser(UserMapper.userToDto(user))
        );
    }
}
