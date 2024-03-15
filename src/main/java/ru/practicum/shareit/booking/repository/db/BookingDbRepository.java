package ru.practicum.shareit.booking.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDbRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdOrderByIdDesc(Integer bookerId);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(
            Integer bookerId, LocalDateTime start, LocalDateTime end
    );

    List<Booking> findAllByBookerIdAndStatusAndEndIsBeforeOrderByIdDesc(
            Integer bookerId, Status status, LocalDateTime start
    );

    List<Booking> findAllByBookerIdAndStatusInAndStartIsAfterOrderByIdDesc(
            Integer bookerId, List<Status> status, LocalDateTime pb
    );

    List<Booking> findAllByBookerIdAndStatusOrderByIdDesc(Integer bookerId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByIdDesc(Integer ownerId);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdDesc(
            Integer ownerId, LocalDateTime start, LocalDateTime end
    );

    List<Booking> findAllByItemOwnerIdAndStatusAndEndIsBeforeOrderByIdDesc(
            Integer ownerId, Status status, LocalDateTime start
    );

    List<Booking> findAllByItemOwnerIdAndStatusInAndStartIsAfterOrderByIdDesc(
            Integer ownerId, List<Status> status, LocalDateTime start
    );

    List<Booking> findAllByItemOwnerIdAndStatusOrderByIdDesc(Integer bookerId, Status status);

    List<Booking> findByItemIdAndStartIsBeforeOrderByEndDesc(Integer itemId, LocalDateTime now);

    List<Booking> findByItemIdAndStartIsAfterAndStatusOrderByStartAsc(
            Integer itemId, LocalDateTime now, Status status
    );

    List<Booking> findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(
            Integer itemId, Integer userId, Status status, LocalDateTime start
    );
}
