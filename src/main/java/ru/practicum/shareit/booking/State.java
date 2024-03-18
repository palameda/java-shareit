package ru.practicum.shareit.booking;

/**
 * Критерий отбора бронирований. Может принимать одно из следующих значений:
 * <ul>
 *     <li>ALL - для отбора всех записей;</li>
 *     <li>CURRENT - для отбора текущих записей;</li>
 *     <li>PAST - для отбора прежних записей;</li>
 *     <li>FUTURE - для отбора будущих записей;</li>
 *     <li>WAITING - для отбора записей со статусом {@link Status#WAITING};</li>
 *     <li>REJECTED - для отбора записей со статусом {@link Status#REJECTED}.</li>
 * </ul>
 * @see Status
 */
public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED
}
