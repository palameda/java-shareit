package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Data Transfer Object для сущности "Бронирование" используется в качестве ответа на запрос.
 * Dto-класс BookingResponseDto содержит поля:
 * <ul>
 *     <li>id - уникальный идентификатор бронирования, тип {@link Integer};</li>
 *     <li>start - дата и время начала бронирования, тип {@link LocalDateTime};</li>
 *     <li>end - дата и время окончания бронирования, тип {@link LocalDateTime};</li>
 *     <li>item - вещь, которую пользователь бронирует, тип {@link Item};</li>
 *     <li>booker - пользователь, который осуществляет бронирование, тип {@link User};</li>
 *     <li>status - статус бронирования, тип {@link Status}.</li>
 * </ul>
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Integer id;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    @NotNull
    private Item item;
    @NotNull
    private User booker;
    private Status status;
}
