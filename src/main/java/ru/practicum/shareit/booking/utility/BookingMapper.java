package ru.practicum.shareit.booking.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * Утилитарный класс BookingMapper предназначен для конвертации объектов, хранящихся в репозитории,
 * в объекты для передачи данных и обратно.
 * @see Booking
 * @see BookingRequestDto
 */
@UtilityClass
public class BookingMapper {

    /**
     * Статичный метод bookingToDto конвертирует объект класса Booking в объект класса BookingRequestDto
     * @param booking объект, содержащий данные об аренде
     * @return объект класса {@link BookingRequestDto}, полученный в результате преобразования {@link Booking}
     */
    public static BookingRequestDto bookingToRequestDto(Booking booking) {
        return BookingRequestDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .userId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    /**
     * Статичный метод BookingResponseDto конвертирует объект класса Booking в объект класса BookingResponseDto
     * @param booking объект, содержащий данные об аренде
     * @return объект класса {@link BookingResponseDto}, полученный в результате преобразования {@link Booking}
     */
    public static BookingResponseDto bookingToResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    /**
     * Статичный метод dtoToBooking конвертирует объект класса BookingDto в объект класса Booking
     * @param bookingRequestDto dto объект, содержащий данные об аренде
     * @param item объект, содержащий данные о вещи
     * @param booker объект, содержащий данные об арендаторе
     * @return объект класса {@link Booking}, полученный в результате преобразования {@link BookingRequestDto}
     */
    public static Booking requestDtoToBooking(BookingRequestDto bookingRequestDto, Item item, User booker) {
        return Booking.builder()
                .id(bookingRequestDto.getId())
                .item(item)
                .booker(booker)
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .status(bookingRequestDto.getStatus())
                .build();
    }
}
