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
 * TODO Sprint add-bookings.
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
