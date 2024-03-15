package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtomicBookingDto {
    private Integer bookingId;
    private Integer bookerId;
}