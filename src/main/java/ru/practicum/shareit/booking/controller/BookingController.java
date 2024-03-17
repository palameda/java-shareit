package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<Booking> findAllBookingsForBooker(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings от пользователя с id {}", userId);
        return bookingService.findAllBookingsForBooker(userId, state);
    }

    @GetMapping("/{id}")
    public Booking findBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable("id") Integer bookingId) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings/{} от пользователя с id {}", userId, userId);
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<Booking> findBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings/owner от пользователя с id {} для состояния {}", userId, state);
        return bookingService.findAllBookingForOwner(userId, state);
    }

    @PostMapping()
    public Booking save(@Valid @RequestBody BookingDto booking, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        booking.setUserId(userId);
        booking.setStatus(Status.WAITING);
        log.info("Контроллер: POST-запрос по эндпоинту /bookings от пользователя с id {}", userId);
        return bookingService.save(booking);
    }

    @PatchMapping("/{id}")
    public Booking update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @PathVariable("id") Integer bookingId,
                          @RequestParam String approved) {
        Status status;
        if (approved.equals("true")) {
            status = Status.APPROVED;
        } else if (approved.equals("false")) {
            status = Status.REJECTED;
        } else {
            throw new NotFoundException("Ошибка статуса бронирования");
        }
        log.info("Контроллер: PATCH-запрос по эндпоинту /bookings/{} от пользователя с id {}", userId, userId);
        return bookingService.update(bookingId, userId, status);
    }
}
