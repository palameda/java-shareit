package ru.practicum.shareit.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImplementation;

import java.util.List;
import java.util.Optional;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {
    private UserDbRepository userRepository;
    private UserService userService;

    private static User user;
    private static UserDto userRequest;
    private static UserDto userResponse;

    @BeforeAll()
    public static void createUser() {
        user = User.builder()
                .id(1)
                .name("userModel")
                .email("model@user.com")
                .build();
        userRequest = UserDto.builder()
                .id(1)
                .name("userDto")
                .email("dto@user.com")
                .build();
        userResponse = UserDto.builder()
                .id(1)
                .name("userDto")
                .email("dto@user.com")
                .build();
    }

    @BeforeEach
    public void setup() {
        userRepository = mock(UserDbRepository.class);
        userService = new UserServiceImplementation(userRepository);
    }

    @Test
    @DisplayName("Тест метода findById с корректным id")
    public void testShouldFindUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        assertThat(userService.findById(1).getName(), equalTo(user.getName()));
        assertThat(userService.findById(1).getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Тест метода findById с некорректным id")
    public void testShouldFailFindUserById() {
        NotFoundException exception = Assertions.assertThrows(
            NotFoundException.class,
                () -> userService.findById(1)
        );
        assertThat(exception.getMessage(), equalTo("Пользователь с id " + user.getId() + " не найден"));
    }

    @Test
    @DisplayName("Тест метода findAll")
    public void testShouldFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        assertThat(userService.findAll().isEmpty(), is(Boolean.FALSE));
        assertThat(userService.findAll().size(), equalTo(1));
        assertThat(userService.findAll().get(0).getId(), equalTo(user.getId()));
        assertThat(userService.findAll().get(0).getName(), equalTo(user.getName()));
        assertThat(userService.findAll().get(0).getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Тест метода deleteUser")
    public void testShouldDeleteUserById() {
        Assertions.assertDoesNotThrow(() -> userService.deleteUser(1));
    }

    @Test
    @DisplayName("Тест метода saveUser")
    public void testShouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        assertThat(userService.saveUser(userResponse).getId(), equalTo(user.getId()));
        assertThat(userService.saveUser(userResponse).getName(), equalTo(user.getName()));
        assertThat(userService.saveUser(userResponse).getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Тест метода saveUser")
    public void testShouldFailSaveUserBecauseOfDuplicateData() {
        when(userRepository.save(any(User.class))).thenThrow(new DuplicateDataException("errorMessage"));
        DuplicateDataException exception = Assertions.assertThrows(
                DuplicateDataException.class,
                () -> userRepository.save(user)
        );
        assertThat(exception.getMessage(), equalTo("errorMessage"));
    }

    @Test
    @DisplayName("Тест метода updateUser для существующего пользователя")
    public void testShouldUpdateExistingUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        user.setEmail("new@user.com");
        assertThat(userService.updateUser(userResponse).getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Тест метода updateUser для несуществующего пользователя")
    public void testShouldFailUpdateNonExistingUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        userRequest.setId(2);
        NotFoundException exception = Assertions.assertThrows(
            NotFoundException.class,
                () -> userService.updateUser(userRequest)
        );
        assertThat(exception.getMessage(), equalTo("Пользователь с id " + userRequest.getId() + " не найден"));
    }
}
