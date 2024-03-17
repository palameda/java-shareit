package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * Интерфейс BookingService содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой сервиса для сущности {@link Booking}.
 */
public interface BookingService {

    /**
     * Метод для сохраниния информации о бронировании.
     * @param booking объект класс {@link Booking}
     * @return объект класс {@link Booking}
     */
    public Booking save(BookingDto booking);

    /**
     * Метод для изменения информации о бронировании.
     * @param bookingId идентификатор бронирования
     * @param bookerId идентификатор арендатора
     * @param status статус бронирования
     * @return объект класса {@link Booking}
     * @see Status
     */
    public Booking update(Integer bookingId, Integer bookerId, Status status);

    /**
     * Метод для поиска арендованной вещи арендатором
     * @param bookingId идентификатор бронирования
     * @param bookerId идентификатор арендатора
     * @return объект класса {@link Booking}
     */
    public Booking findBookingById(Integer bookingId, Integer bookerId);

    /**
     * Метод позволяет владельцу вещей получить информацию о бронях на них.
     * @param ownerId идентификатор владельца
     * @param state критерий отбора
     * @return список вещей, отобранных по критерию {@link State} и преобразованных в {@link Booking}
     */
    public List<Booking> findAllBookingForOwner(Integer ownerId, String state);

    /**
     * Метод позволяет арендатору вещей получить информацию о своих бронях.
     * @param bookerId идентификатор арендатора
     * @param state критерий отбора
     * @return список вещей, отобранных по критерию {@link State} и преобразованных в {@link Booking}
     */
    public List<Booking> findAllBookingsForBooker(Integer bookerId, String state);
}
