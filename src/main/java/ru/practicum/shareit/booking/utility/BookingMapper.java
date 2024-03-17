package ru.practicum.shareit.booking.utility;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * Утилитарный класс BookingMapper предназначен для конвертации объектов, хранящихся в репозитории,
 * в объекты для передачи данных и обратно.
 * @see Booking
 * @see BookingDto
 */
@UtilityClass
public class BookingMapper {

    /**
     * Статичный метод bookingToDto конвертирует объект класса Booking в объект класса BookingDto
     * @param booking объект, содержащий данные об аренде
     * @return объект класса {@link BookingDto}, полученный в результате преобразования {@link Booking}
     */
    public static BookingDto bookingToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .userId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    /**
     * Статичный метод dtoToBooking конвертирует объект класса BookingDto в объект класса Booking
     * @param bookingDto dto объект, содержащий данные об аренде
     * @param item объект, содержащий данные о вещи
     * @param booker объект, содержащий данные об арендаторе
     * @return объект класса {@link Booking}, полученный в результате преобразования {@link BookingDto}
     */
    public static Booking dtoToBooking(BookingDto bookingDto, Item item, User booker) {
        return Booking.builder()
                .id(bookingDto.getId())
                .item(item)
                .booker(booker)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
    }
}
