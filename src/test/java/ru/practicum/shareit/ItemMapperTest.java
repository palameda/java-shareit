package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingReference;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.utility.ItemMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemMapperTest {
    User owner = User.builder()
            .id(1)
            .name("owner")
            .email("owner@user.com")
            .build();
    User booker = User.builder()
            .id(2)
            .name("booker")
            .email("booker@user.com")
            .build();
    Item item = Item.builder()
            .id(1)
            .name("item")
            .ownerId(owner.getId())
            .description("description")
            .available(true)
            .request(null)
            .build();
    Booking booking = Booking.builder()
            .id(1)
            .booker(booker)
            .item(item)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusHours(1))
            .status(Status.WAITING)
            .build();

    @Test
    @DisplayName("Проверка мэппинга в BookingReference")
    void itemToBookingReference_whenReceiveBooking_ThenReturnBookingReference() {
        BookingReference bookingReference = ItemMapper.itemToBookingReference(booking);
        Assertions.assertEquals(bookingReference.getBookerId(), booker.getId());
    }
}
