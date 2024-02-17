package ru.practicum.shareit.user;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.ram.InMemoryUserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImplementation;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;
    private UserDto user1;
    private UserDto user2;

    @BeforeEach
    public void initTest() {
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        userService = new UserServiceImplementation(userRepository);

        user1 = UserDto.builder()
                .name("User1")
                .email("user1@email.user.com")
                .build();

        user2 = UserDto.builder()
                .name("User2")
                .email("user2@email.user.com")
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Сохранение нового пользователя")
    public void testShouldSaveUser() {
        UserDto savedUser = userService.saveUser(user1);
        Assertions.assertEquals(savedUser.getId(), 1, "Id пользователя не совпадает с ожидаемым");
        Assertions.assertEquals(savedUser.getName(), user1.getName(), "Имя пользователя не совпадает с ожидаемым");
        Assertions.assertEquals(savedUser.getEmail(), user1.getEmail(), "Email пользователя не совпадает с ожидаемым");
    }

    @Test
    @Order(2)
    @DisplayName("Сохранение пользователя с дубликатом свойства email")
    public void testShouldNotSaveUserIfEmailAlreadyExists() {
        UserDto newUser = UserDto.builder()
                .name("newUser")
                .email(user1.getEmail())
                .build();
        userService.saveUser(user1);
        DuplicateDataException exception = Assertions.assertThrows(
                DuplicateDataException.class,
                () -> userService.saveUser(newUser)
        );
        Assertions.assertEquals(exception.getMessage(), "Email уже зарегистрирован в системе",
                "Сообщение об ошибке не совпадет с ожидаемым"
        );
    }

    @Test
    @Order(3)
    @DisplayName("Обновление данных пользователя")
    public void testShouldUpdateUser() {
        UserDto savedUser = userService.saveUser(user1);
        Assertions.assertEquals(savedUser.getName(), user1.getName(),
                "Имя пользователя не совпадает с ожидаемым"
        );
        user1.setId(savedUser.getId());
        user1.setName("updatedUser1");
        Assertions.assertEquals(userService.updateUser(user1).getName(), user1.getName(),
                "Имя пользователя не совпадает с ожидаемым"
        );
    }

    @Test
    @Order(4)
    @DisplayName("Обновление пользователя с дубликатом свойства email")
    public void testShouldNotUpdateUserIfEmailAlreadyExists() {
        userService.saveUser(user1);
        Integer id = userService.saveUser(user2).getId();
        user2.setId(id);
        user2.setEmail(user1.getEmail());
        DuplicateDataException exception = Assertions.assertThrows(
                DuplicateDataException.class,
                () -> userService.updateUser(user2)
        );
        Assertions.assertEquals(exception.getMessage(), "Email уже зарегистрирован в системе",
                "Сообщение об ошибке не совпадет с ожидаемым"
        );
    }
    @Test
    @Order(5)
    @DisplayName("Получение списка всех пользователей")
    public void testShouldReturnListOfUser() {
        Assertions.assertEquals(userService.findAll().size(), 0,
                "Размеры списков не совпадают"
        );
        userService.saveUser(user1);
        userService.saveUser(user2);
        Assertions.assertEquals(userService.findAll().size(), 2,
                "Размеры списков не совпадают"
        );
    }

    @Test
    @Order(6)
    @DisplayName("Получение пользователя по id")
    public void testShouldReturnUserById() {
        Integer id = userService.saveUser(user1).getId();
        Optional<UserDto> receivedUser = Optional.of(userService.findById(id));
        Assertions.assertTrue(receivedUser.isPresent(), "При запросе пользователя по id получен null");
        Assertions.assertEquals(receivedUser.get().getId(), id, "Id не совпадает с ожидаемым");
        Assertions.assertEquals(receivedUser.get().getName(), user1.getName(), "Имя не сопадает с ожидаемым");
        Assertions.assertEquals(receivedUser.get().getEmail(), user1.getEmail(), "Email не сопадает с ожидаемым");
    }

    @Test
    @Order(7)
    @DisplayName("Получение пользователя по некорректному id")
    public void testShouldNotReturnUserInCaseOfWrongId() {
        Integer wrongId = 999;
        userService.saveUser(user1);
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.findById(wrongId)
        );
        Assertions.assertEquals(exception.getMessage(), String.format("Пользователь с id " + wrongId + " не найден"),
                "Сообщение об ошибке не совпадет с ожидаемым"
        );
    }

    @Test
    @Order(8)
    @DisplayName("Удаление пользователя по id")
    public void testShouldDeleteUserById() {
        Integer id1 = userService.saveUser(user1).getId();
        Integer id2 = userService.saveUser(user2).getId();
        Assertions.assertEquals(userService.findAll().size(), 2, "Размер списка не сопадает с ожидаемым");
        userService.deleteUser(id1);
        Assertions.assertEquals(userService.findAll().size(), 1, "Размер списка не сопадает с ожидаемым");
        userService.deleteUser(id2);
        Assertions.assertEquals(userService.findAll().size(), 0, "Размер списка не сопадает с ожидаемым");

    }
}
