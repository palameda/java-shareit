package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс BookingController - контроллер, который обрабатывает GET, POST, PATCH и DELETE-методы запросов по эндпоинту /bookings.
 * Взаимодествует со слоем сервиса с помощью внедрённой зависимости {@link BookingService}.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    /**
     * Метод findAllBookingsForBooker обрабатывает GET-метод запроса к эндпоинту /bookings,
     * обращается к методу {@link BookingService#findAllBookingsForBooker(Integer, String)}.
     * @param userId идентификатор арендатора вещей
     * @param state необязательный параметр, выражающий критерий для отбора арендованных вещей (по умолчанию ALL)
     * @return список всех арендованных вещей пользователя, преобразованных в {@link Booking}.
     */
    @GetMapping
    public List<Booking> findAllBookingsForBooker(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings от пользователя с id {}", userId);
        return bookingService.findAllBookingsForBooker(userId, state);
    }

    /**
     * Метод findBookingById обрабатывает GET-метод запроса к эндпоинту /bookings/id,
     * обращается к методу {@link BookingService#findBookingById(Integer, Integer)}.
     * @param userId идентификатор пользователя, который совершает запрос
     * @param bookingId идентификатор арендованной вещи
     * @return объект класса {@link Booking}
     */
    @GetMapping("/{id}")
    public Booking findBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable("id") Integer bookingId) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings/{} от пользователя с id {}", userId, userId);
        return bookingService.findBookingById(bookingId, userId);
    }

    /**
     * Метод findBookingsForOwner обрабатывает GET-метод запроса к эндпоинту /bookings/owner, обращается к
     * методу {@link BookingService#findAllBookingForOwner(Integer, String)}.
     * @param userId идентификатор арендодателя (владельца) вещи(ей)
     * @param state необязательный параметр, выражающий критерий для отбора арендованных вещей (по умолчанию ALL)
     * @return список всех арендованных вещей арендодателя, преобразованных в {@link Booking}.
     */
    @GetMapping("/owner")
    public List<Booking> findBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL") String state) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings/owner от пользователя с id {} для состояния {}", userId, state);
        return bookingService.findAllBookingForOwner(userId, state);
    }

    /**
     * Метод save обрабатывает POST-метод запроса к эндпоинту /bookings,
     * обращается к методу {@link BookingService#save(BookingDto)}
     * @param booking объект класса BookingDto
     * @param userId идентификатор арендодателя (владельца) вещи
     * @return объект класса {@link Booking}
     */
    @PostMapping()
    public Booking save(@Valid @RequestBody BookingDto booking, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        booking.setUserId(userId);
        booking.setStatus(Status.WAITING);
        log.info("Контроллер: POST-запрос по эндпоинту /bookings от пользователя с id {}", userId);
        return bookingService.save(booking);
    }

    /**
     * Метод update обрабатывает PATCH-метод запроса к эндпоинту /bookings,
     * обращается к методу {@link BookingService#update(Integer, Integer, Status)}
     * @param userId идентификатор арендодателя (владельца) вещи
     * @param bookingId идентификатор аренды
     * @param approved статус аренды
     * @return объект класса {@link Booking}
     */
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
