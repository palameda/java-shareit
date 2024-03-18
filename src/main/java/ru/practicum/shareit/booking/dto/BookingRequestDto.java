package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Data Transfer Object для сущности "Бронирование"
 * Dto-класс Booking содержит поля:
 * <ul>
 *     <li>id - уникальный идентификатор бронирования, тип {@link Integer}</li>
 *     <li>itemId - идентификатор бронируемой вещи, тип {@link Integer}</li>
 *     <li>userId - идентификатор арендатора вещи, тип {@link Integer}</li>
 *     <li>status - статус бронирования, тип {@link Status}</li>
 *     <li>start - дата и время начала аренды, тип {@link LocalDateTime}</li>
 *     <li>end - дата и время окончания аренды, тип {@link LocalDateTime}</li>
 * </ul>
 */

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Integer id;
    @NotNull
    private Integer itemId;
    private Integer userId;
    private Status status;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
