package ru.practicum.shareit.booking;

/**
 * Статус бронирования. Может принимать одно из следующих значений:
 * <ul>
 *     <li>WAITING - новое бронирование, ожидает одобрения;</li>
 *     <li>APPROVED - бронирование подтверждено владельцем;</li>
 *     <li>REJECTED - бронирование отклонено владельцем;</li>
 *     <li>CANCELED - бронирование отменено владельцем.</li>
 * </ul>
 */
public enum Status {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
