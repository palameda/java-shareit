package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.booking.utility.BookingMapper;
import ru.practicum.shareit.exception.DenialOfAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public Booking save(BookingDto booking) {
        checkBooking(booking);
        return bookingRepository.save(BookingMapper.dtoToBooking(booking, item, user));
    }

    @Transactional
    @Override
    public Booking update(Integer bookingId, Integer bookerId, Status status) {
        Booking booking = checkBookingById(bookingId);
        checkBooking(BookingMapper.bookingToDto(booking));
        if (!Objects.equals(booking.getItem().getOwnerId(), bookingId)) {
            throw new NotFoundException("Пользователю с id " + bookerId + " отказано в редактировании брони. " +
                    "Причина: \"не является владельцем вещи\"");
        }
        if (booking.getStatus().equals(status)) {
            throw new ValidationException("Для бронирования с id " + bookingId + " уже установлен статус " + status);
        }
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking findBookingById(Integer bookingId, Integer bookerId) {
        Booking booking = checkBookingById(bookingId);
        if (!((Objects.equals(booking.getItem().getOwnerId(), bookerId)) || (Objects.equals(booking.getBooker().getId(), bookerId))))
            throw new DenialOfAccessException("Пользователь с id " + bookerId + " не может выполнять просмотр бронироввания с id " +
                    bookingId);
        return booking;
    }

    @Override
    public List<Booking> findAllBookingForOwner(Integer ownerId, State state) {
        checkUserById(ownerId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByIdDesc(ownerId);
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdDesc(
                        ownerId, LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndStatusAndEndIsBeforeOrderByIdDesc(
                        ownerId, Status.APPROVED, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStatusInAndStartIsAfterOrderByIdDesc(
                        ownerId, List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdDesc(ownerId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdDesc(ownerId, Status.REJECTED);
            default:
                throw new NotFoundException("Состояние " + state + " не зарегистрировано в системе");
        }
    }

    @Override
    public List<Booking> findAllBookingsForBooker(Integer bookerId, State state) {
        checkUserById(bookerId);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByIdDesc(bookerId);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(bookerId,
                        LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByBookerIdAndStatusAndEndIsBeforeOrderByIdDesc(bookerId,
                        Status.APPROVED, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStatusInAndStartIsAfterOrderByIdDesc(bookerId,
                        List.of(Status.APPROVED, Status.WAITING), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(bookerId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(bookerId, Status.REJECTED);
            default:
                throw new NotFoundException("Состояние " + state + " не зарегистрировано в системе");
        }
    }

    private Item checkItem(BookingDto booking) {
        return itemRepository.findById(booking.getItemId()).orElseThrow(
                        () -> new NotFoundException("Вещь " + booking.toString() + " не зарегистрирована в системе")
                );
    }

    private User checkUser(BookingDto booking) {
        return userRepository.findById(booking.getUserId()).orElseThrow(
                        () -> new NotFoundException("Пользователь c id " + booking.getUserId() + " не зарегистрирован в системе")
                );
    }

    private void checkUserById(Integer userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь c id " + userId + " не зарегистрирован в системе")
        );
    }

    private Booking checkBookingById(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Бронирование c id " + bookingId + " не найдено в системе")
        );
    }

    private void checkBooking(BookingDto booking) {

        item = checkItem(booking);
        user = checkUser(booking);

        if (booking.getStart() == null || booking.getEnd() == null)
            throw new ValidationException("Ошибка в данных даты начала или даты окончания бронирования");

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd()))
            throw new ValidationException("Даты начала и окончания бронирования не должны совпадать");

        if (!item.getAvailable())
            throw new ValidationException("Бронь вещи " + booking.toString() + " недоступна");

        if (Objects.equals(booking.getUserId(), item.getOwnerId()))
            throw new DenialOfAccessException("Владелец не может бронировать свои вещи");
    }

}
