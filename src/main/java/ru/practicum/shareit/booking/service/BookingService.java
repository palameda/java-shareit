package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    public BookingDto save(BookingDto booking);

    public BookingDto update(Integer bookingId, Integer bookerId, Status status);

    public BookingDto findBookingById(Integer bookingId, Integer bookerId);

    public List<BookingDto> findAllBookingForOwner(Integer ownerId, State state);

    public List<BookingDto> findAllBookingsForBooker(Integer bookerId, State state);
}
