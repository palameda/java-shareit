package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class DatelessBooking {
    private Integer bookingId;

    private Item item;

    private User booker;

    private Status status;
}
