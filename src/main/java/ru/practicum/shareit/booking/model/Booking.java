package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <p>Data-класс <strong>Booking</strong> содержит информацию о сущности "бронирование" и имеет поля:</p>
 * <ul>
 *     <li>id - уникальный идентификатор бронирования, тип {@link Integer};</li>
 *     <li>start - дата и время начала бронирования, тип {@link LocalDateTime};</li>
 *     <li>end - дата и время окончания бронирования, тип {@link LocalDateTime};</li>
 *     <li>item - вещь, которую пользователь бронирует, тип {@link Item};</li>
 *     <li>booker - пользователь, который осуществляет бронирование, тип {@link User};</li>
 *     <li>status - статус бронирования, тип {@link Status}.</li>
 * </ul>
 */

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "date_from", nullable = false)
    private LocalDateTime start;
    @Column(name = "date_to", nullable = false)
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User booker;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
