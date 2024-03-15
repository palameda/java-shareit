package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class DatelessDto {
    private Integer bookingId;

    private Item item;

    private User booker;

    private Status status;
}
