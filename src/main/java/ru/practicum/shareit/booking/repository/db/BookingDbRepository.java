package ru.practicum.shareit.booking.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс BookingDbRepository содержит сигнатуры методов для работы с сущностью {@link Booking}.
 * Расширяет {@link JpaRepository}, который реализует основные CRUD-операции.
 */
public interface BookingDbRepository extends JpaRepository<Booking, Integer> {

    /**
     * Метод позволяет получить все бронирования для арендатора.
     * @param bookerId идентификатор арендатора
     * @return список арендованных вещей, преобразованных в {@link Booking}
     */
    List<Booking> findAllByBookerIdOrderByIdDesc(Integer bookerId);

    /**
     * Метод позволяет получить все текущие бронирования для арендатора.
     * @param bookerId идентификатор арендатора
     * @param start дата и время начала бронирования
     * @param end дата и время окончания бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     */
    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(
            Integer bookerId, LocalDateTime start, LocalDateTime end
    );

    /**
     * Метод позволяет получить все прежние бронирования для арендатора.
     * @param bookerId идентификатор арендатора
     * @param status статус бронирования
     * @param start дата и время начала бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findAllByBookerIdAndStatusAndEndIsBeforeOrderByIdDesc(
            Integer bookerId, Status status, LocalDateTime start
    );

    /**
     * Метод позволяет получить все будущие бронирования для арендатора.
     * @param bookerId идентификатор арендатора
     * @param status статус бронирования
     * @param end дата и время окончания бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findAllByBookerIdAndStatusInAndStartIsAfterOrderByIdDesc(
            Integer bookerId, List<Status> status, LocalDateTime end
    );

    /**
     * Метод позволяет получить все бронирования с определённым статусом для арендатора.
     * @param bookerId идентификатор арендатора
     * @param status статус бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findAllByBookerIdAndStatusOrderByIdDesc(Integer bookerId, Status status);

    /**
     * Метод позволяет владельцу вещей получить информацию о всех бронях на них
     * @param ownerId идентификатор арендодатора
     * @return список арендованных вещей, преобразованных в {@link Booking}
     */
    List<Booking> findAllByItemOwnerIdOrderByIdDesc(Integer ownerId);

    /**
     *  Метод позволяет владельцу вещей получить информацию о текущих бронях на них
     * @param ownerId идентификатор арендодатора
     * @param start дата и время начала бронирования
     * @param end дата и время окончания бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     */
    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdDesc(
            Integer ownerId, LocalDateTime start, LocalDateTime end
    );

    /**
     * Метод позволяет владельцу вещей получить информацию о прежних бронях на них
     * @param ownerId идентификатор арендодатора
     * @param status статус бронирования
     * @param start дата и время начала бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findAllByItemOwnerIdAndStatusAndEndIsBeforeOrderByIdDesc(
            Integer ownerId, Status status, LocalDateTime start
    );

    /**
     * Метод позволяет владельцу вещей получить информацию о будущих бронях на них
     * @param ownerId идентификатор арендодатора
     * @param status статус бронирования
     * @param end дата и время окончания бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findAllByItemOwnerIdAndStatusInAndStartIsAfterOrderByIdDesc(
            Integer ownerId, List<Status> status, LocalDateTime end
    );

    /**
     * Метод позволяет владельцу вещей получить информацию о бронях на них с определённым статусом
     * @param ownerId идентификатор арендодатора
     * @param status статус бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findAllByItemOwnerIdAndStatusOrderByIdDesc(Integer ownerId, Status status);

    /**
     * Метод позволяет получить информацию о забронированных к настоящему времени вещах.
     * @param itemId идентификатор вещи
     * @param now настоящее время бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     */
    List<Booking> findByItemIdAndStartIsBeforeOrderByEndDesc(Integer itemId, LocalDateTime now);


    /**
     * Метод позволяет получить информацию будующих бронированиях с определённым статусом.
     * @param itemId идентификатор вещи
     * @param now настоящее время бронирования
     * @param status статус бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findByItemIdAndStartIsAfterAndStatusOrderByStartAsc(
            Integer itemId, LocalDateTime now, Status status
    );

    /**
     * Метод позволяет получить информацию о бронированиях с определённым статусом
     * для определённого пользователя .
     * @param itemId идентификатор вещи
     * @param userId идентификатор пользователя
     * @param status статус бронирования
     * @param start дата ивремя начала бронирования
     * @return список арендованных вещей, преобразованных в {@link Booking}
     * @see Status
     */
    List<Booking> findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(
            Integer itemId, Integer userId, Status status, LocalDateTime start
    );
}
