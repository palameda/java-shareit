package ru.practicum.shareit.user.utility;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * <p>Утилитарный класс UserMapper предназначен для конвертации объектов, хранящихся в репозитории,
 * в объекты для передачи данных и обратно.</p>
 * @see User
 * @see UserDto
 */

@UtilityClass
public class UserMapper {

    /**
     * Статичный метод dtoToUser конвертирует объект класса {@link UserDto} в объект класса {@link User}.
     * @param userDto объект класса {@link UserDto}, содержащий данные о пользователе
     * @return объект класса {@link User}, полученный в результате преобразования userDto
     */
    public static User dtoToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * Статичный метод userToDto конвертирует объект класса {@link User} в объект класса {@link UserDto}
     * @param user объект класса {@link User}, содержащий данные о пользователе
     * @return объект класса {@link UserDto}, полученный в результате преобразования user
     */
    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
