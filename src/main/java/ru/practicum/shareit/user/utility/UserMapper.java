package ru.practicum.shareit.user.utility;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * <p>Утилитарный класс UserMapper предназначен для конвертации объектов, хранящихся в репозитории,
 * в объекты для передачи данных и обратно.</p>
 */

@UtilityClass
public class UserMapper {

    /**
     * Статичный метод DtoToUser конвертирует объект userDto в объект класса User
     * @param userDto
     * @return User
     */
    public static User DtoToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * Статичный метод UserToDto конвертирует объект user в объект класса UserDto
     * @param user
     * @return UserDto
     */
    public static UserDto UserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
