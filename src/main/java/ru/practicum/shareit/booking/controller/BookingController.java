package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Класс BookingController - контроллер, который обрабатывает GET, POST, PATCH и DELETE-методы запросов по эндпоинту /bookings.
 * Взаимодествует со слоем сервиса с помощью внедрённой зависимости {@link BookingService}.
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private static final String DEFAULT_PAGE_SIZE = "10";
    private final BookingService bookingService;

    /**
     * Метод findAllBookingsForBooker обрабатывает GET-метод запроса к эндпоинту /bookings,
     * обращается к методу {@link BookingService#findAllBookingsForBooker(Integer, String, Pageable)}.
     * @param userId идентификатор арендатора вещей
     * @param state необязательный параметр, выражающий критерий для отбора арендованных вещей (по умолчанию ALL)
     * @return список всех арендованных вещей пользователя, преобразованных в {@link BookingResponseDto}.
     */
    @GetMapping
    public List<BookingResponseDto> findAllBookingsForBooker(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                             @RequestParam(defaultValue = "ALL") String state,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                             @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Positive Integer size) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings от пользователя с id {}", userId);
        Pageable page = PageRequest.of((from / size), size, Sort.by("id").descending());
        return bookingService.findAllBookingsForBooker(userId, state, page);
    }

    /**
     * Метод findBookingById обрабатывает GET-метод запроса к эндпоинту /bookings/id,
     * обращается к методу {@link BookingService#findBookingById(Integer, Integer)}.
     * @param userId идентификатор пользователя, который совершает запрос
     * @param bookingId идентификатор арендованной вещи
     * @return объект класса {@link BookingResponseDto}
     */
    @GetMapping("/{id}")
    public BookingResponseDto findBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @PathVariable("id") Integer bookingId) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings/{} от пользователя с id {}", userId, userId);
        return bookingService.findBookingById(bookingId, userId);
    }

    /**
     * Метод findBookingsForOwner обрабатывает GET-метод запроса к эндпоинту /bookings/owner, обращается к
     * методу {@link BookingService#findAllBookingForOwner(Integer, String, Pageable)}.
     * @param userId идентификатор арендодателя (владельца) вещи(ей)
     * @param state необязательный параметр, выражающий критерий для отбора арендованных вещей (по умолчанию ALL)
     * @return список всех арендованных вещей арендодателя, преобразованных в {@link BookingResponseDto}.
     */
    @GetMapping("/owner")
    public List<BookingResponseDto> findBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Positive Integer size) {
        log.info("Контроллер: GET-запрос по эндпоинту /bookings/owner от пользователя с id {} для состояния {}", userId, state);
        Pageable page = PageRequest.of((from / size), size, Sort.by("id").descending());
        return bookingService.findAllBookingForOwner(userId, state, page);
    }

    /**
     * Метод save обрабатывает POST-метод запроса к эндпоинту /bookings,
     * обращается к методу {@link BookingService#save(BookingRequestDto)}
     * @param booking объект класса BookingRequestDto
     * @param userId идентификатор арендодателя (владельца) вещи
     * @return объект класса {@link BookingResponseDto}
     */
    @PostMapping()
    public BookingResponseDto save(@Valid @RequestBody BookingRequestDto booking, @RequestHeader("X-Sharer-User-Id") Integer userId) {
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
     * @return объект класса {@link BookingResponseDto}
     */
    @PatchMapping("/{id}")
    public BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Integer userId,
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
