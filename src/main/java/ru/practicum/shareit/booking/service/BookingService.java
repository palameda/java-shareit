package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    public Booking save(BookingDto booking);

    public Booking update(Integer bookingId, Integer bookerId, Status status);

    public Booking findBookingById(Integer bookingId, Integer bookerId);

    public List<Booking> findAllBookingForOwner(Integer ownerId, State state);

    public List<Booking> findAllBookingsForBooker(Integer bookerId, State state);
}
