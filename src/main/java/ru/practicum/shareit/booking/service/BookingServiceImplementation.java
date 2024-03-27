package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.booking.utility.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс BookingServiceImplementation реализует методы интерфейса {@link BookingService}.
 * Описывает логику работы приложения с сущностью {@link Booking}
 * @see UserDbRepository
 * @see ItemDbRepository
 * @see BookingDbRepository
 * @see Item
 * @see User
 * @see BookingRequestDto
 * @see BookingResponseDto
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BookingServiceImplementation implements BookingService {

    private final UserDbRepository userRepository;
    private final ItemDbRepository itemRepository;
    private final BookingDbRepository bookingRepository;
    private Item item;
    private User user;

    @Transactional
    @Override
    public BookingResponseDto save(BookingRequestDto booking) {
        checkBooking(booking);
        log.info("Сервис: обработка запроска на сохранение бронирования {}", booking.toString());
        return BookingMapper.bookingToResponseDto(
                bookingRepository.save(BookingMapper.requestDtoToBooking(booking, item, user))
        );
    }

    @Transactional
    @Override
    public BookingResponseDto update(Integer bookingId, Integer bookerId, Status status) {
        Booking booking = checkBookingById(bookingId);
        checkBooking(BookingMapper.bookingToRequestDto(booking));
        if (!Objects.equals(booking.getItem().getOwnerId(), bookerId)) {
            throw new NotFoundException("Пользователю с id " + bookerId + " отказано в редактировании брони. " +
                    "Причина: \"не является владельцем вещи\"");
        }
        if (booking.getStatus().equals(status)) {
            throw new ValidationException("Для бронирования с id " + bookingId + " уже установлен статус " + status);
        }
        booking.setStatus(status);
        log.info("Сервис: обработка запроска на изменение бронирования {} пользователем с id {}", booking.toString(), bookerId);
        return BookingMapper.bookingToResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto findBookingById(Integer bookingId, Integer bookerId) {
        Booking booking = checkBookingById(bookingId);
        if (!((Objects.equals(booking.getItem().getOwnerId(), bookerId)) || (Objects.equals(booking.getBooker().getId(), bookerId))))
            throw new NotFoundException("Пользователь с id " + bookerId + " не может выполнять просмотр бронироввания с id " +
                    bookingId);
        log.info("Сервис: обработка запроска на получение бронирования по id {} пользователем с id {}", bookingId, bookerId);
        return BookingMapper.bookingToResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> findAllBookingForOwner(Integer ownerId, String state, Pageable page) {
        checkUserById(ownerId);
        State bookingState = checkState(state);
        log.info("Сервис: обработка запроска на получение владельцем с id {} всех броней с состоянием {}", ownerId, state);
        switch (bookingState) {
            case ALL:
                return bookingRepository.findAllByItemOwnerId(ownerId, page).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdDesc(
                        ownerId, LocalDateTime.now(), LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndStatusAndEndIsBeforeOrderByIdDesc(
                        ownerId, Status.APPROVED, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStatusInAndStartIsAfterOrderByIdDesc(
                        ownerId, List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdDesc(ownerId, Status.WAITING).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdDesc(ownerId, Status.REJECTED).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            default:
                log.info("Сервис: критерия отбора {} не существует", state);
                return Collections.emptyList();
        }
    }

    @Override
    public List<BookingResponseDto> findAllBookingsForBooker(Integer bookerId, String state, Pageable page) {
        checkUserById(bookerId);
        State bookingState = checkState(state);
        log.info("Сервис: обработка запроска на получение пользователем с id {} всех броней с состоянием {}", bookerId, state);
        switch (bookingState) {
            case ALL:
                return bookingRepository.findAllByBookerId(bookerId, page).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(bookerId,
                        LocalDateTime.now(), LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByBookerIdAndStatusAndEndIsBeforeOrderByIdDesc(bookerId,
                        Status.APPROVED, LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStatusInAndStartIsAfterOrderByIdDesc(bookerId,
                        List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now()).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(bookerId, Status.WAITING).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(bookerId, Status.REJECTED).stream()
                        .map(BookingMapper::bookingToResponseDto)
                        .collect(Collectors.toList());
            default:
                log.info("Сервис: критерия отбора {} не существует", state);
                return Collections.emptyList();
        }
    }

    private Item checkItem(BookingRequestDto booking) {
        log.info("Сервис: поиск вещи в системе");
        return itemRepository.findById(booking.getItemId()).orElseThrow(
                        () -> new NotFoundException("Вещь " + booking.toString() + " не зарегистрирована в системе")
                );
    }

    private User checkUser(BookingRequestDto booking) {
        log.info("Сервис: поиск пользователя в системе");
        return userRepository.findById(booking.getUserId()).orElseThrow(
                        () -> new NotFoundException("Пользователь c id " + booking.getUserId() + " не зарегистрирован в системе")
                );
    }

    private void checkUserById(Integer userId) {
        log.info("Сервис: поиск пользователя с id {} в системе", userId);
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь c id " + userId + " не зарегистрирован в системе")
        );
    }

    private Booking checkBookingById(Integer bookingId) {
        log.info("Сервис: поиск бронирования с id {} в системе", bookingId);
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Бронирование c id " + bookingId + " не найдено в системе")
        );
    }

    private void checkBooking(BookingRequestDto booking) {
        item = checkItem(booking);
        user = checkUser(booking);
        log.info("Сервис: поиск бронирования в системе");
        if (booking.getStart() == null || booking.getEnd() == null)
            throw new ValidationException("Ошибка в данных даты начала или даты окончания бронирования");

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd()))
            throw new ValidationException("Даты начала и окончания бронирования не должны совпадать");

        if (!item.getAvailable())
            throw new ValidationException("Бронь вещи " + booking.toString() + " недоступна");

        if (Objects.equals(booking.getUserId(), item.getOwnerId()))
            throw new NotFoundException("Владелец не может бронировать свои вещи");
    }

    private State checkState(String state) {
        log.info("Сервис: валидация состояния бронирования");
        State bookingState;
        try {
            bookingState = State.valueOf(state);
        } catch (IllegalArgumentException exception) {
            log.error("Unknown state: {}", state);
            throw new ValidationException("Unknown state: " + state);
        }
        return bookingState;
    }
}
