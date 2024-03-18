package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * Интерфейс BookingService содежит сигнатуры метотов, опреледяющих функционал,
 * который будет реальзован в классах, описывающих слой сервиса для сущности {@link Booking}.
 */
public interface BookingService {

    /**
     * Метод для сохраниния информации о бронировании.
     * @param booking объект класс {@link BookingRequestDto}
     * @return объект класс {@link BookingResponseDto}
     */
    public BookingResponseDto save(BookingRequestDto booking);

    /**
     * Метод для изменения информации о бронировании.
     * @param bookingId идентификатор бронирования
     * @param bookerId идентификатор арендатора
     * @param status статус бронирования
     * @return объект класса {@link BookingResponseDto}
     * @see Status
     */
    public BookingResponseDto update(Integer bookingId, Integer bookerId, Status status);

    /**
     * Метод для поиска арендованной вещи арендатором
     * @param bookingId идентификатор бронирования
     * @param bookerId идентификатор арендатора
     * @return объект класса {@link BookingResponseDto}
     */
    public BookingResponseDto findBookingById(Integer bookingId, Integer bookerId);

    /**
     * Метод позволяет владельцу вещей получить информацию о бронях на них.
     * @param ownerId идентификатор владельца
     * @param state критерий отбора
     * @return список вещей, отобранных по критерию {@link State} и преобразованных в {@link BookingResponseDto}
     */
    public List<BookingResponseDto> findAllBookingForOwner(Integer ownerId, String state);

    /**
     * Метод позволяет арендатору вещей получить информацию о своих бронях.
     * @param bookerId идентификатор арендатора
     * @param state критерий отбора
     * @return список вещей, отобранных по критерию {@link State} и преобразованных в {@link BookingResponseDto}
     */
    public List<BookingResponseDto> findAllBookingsForBooker(Integer bookerId, String state);
}
