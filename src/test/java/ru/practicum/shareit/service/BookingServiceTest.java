package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImplementation;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookingServiceTest {
    private BookingDbRepository bookingRepository;
    private ItemDbRepository itemRepository;
    private UserDbRepository userRepository;
    private BookingService bookingService;

    private final User user = User.builder()
            .id(1).name("user1")
            .email("user1@user.com")
            .build();
    private final Item item = Item.builder()
            .id(1).name("item1")
            .description("description")
            .available(true)
            .ownerId(1)
            .build();
    private final BookingRequestDto bookingDto = BookingRequestDto.builder()
            .id(1)
            .itemId(1)
            .start(LocalDateTime.now().plusHours(10))
            .end(LocalDateTime.now().plusHours(100))
            .build();
    private final Booking booking = Booking.builder()
            .id(1)
            .item(item)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .status(Status.WAITING)
            .booker(user)
            .build();

    @BeforeEach
    public void setup() {
        bookingRepository = mock(BookingDbRepository.class);
        itemRepository = mock(ItemDbRepository.class);
        userRepository = mock(UserDbRepository.class);
        bookingService = new BookingServiceImplementation(userRepository, itemRepository, bookingRepository);
    }

    @Test
    @DisplayName("Проверка метода save для Booking")
    void testShouldSaveBookingOrThrowNotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        bookingDto.setStart(null);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.save(bookingDto)
        );

        bookingDto.setStart(LocalDateTime.now().plusHours(1000));
        Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.save(bookingDto)
        );

        assert item != null;
        item.setAvailable(false);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.save(bookingDto)
        );
    }

    @Test
    @DisplayName("Проверка метода update для Booking")
    void testShouldUpdateBookingOrThrowValidationException() {
        when(bookingRepository.findById(1)).thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.update(1, 1, Status.REJECTED)
        );
    }

    @Test
    @DisplayName("Проверка метода findBookingById для Booking")
    void testShouldFindBookingByIdOrThrowNotFoundException() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.ofNullable(booking));
        assertThat(bookingService.findBookingById(1, 1).getStatus(), is(Status.WAITING));
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.findBookingById(1, 3)
        );
    }

    @Test
    @DisplayName("Проверка метода findAll для Booking")
    void testShouldFindAllBookingsByItemIdOrItemIdAndBookerId() {
        when(bookingRepository.findByItemIdAndStartIsBeforeOrderByEndDesc(anyInt(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(anyInt(), anyInt(),
                any(Status.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
    }

    @Test
    @DisplayName("Проверка findAllBookingsForBooker для Booking")
    void testShouldFindAllBookingsForBooker() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBookerId(anyInt(), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(anyInt(),
                any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusAndEndIsBeforeOrderByIdDesc(anyInt(),
                any(Status.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusInAndStartIsAfterOrderByIdDesc(anyInt(), any(),
                any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(anyInt(), any(Status.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(anyInt(), any(Status.class)))
                .thenReturn(List.of(booking));

        assertThat(bookingService.findAllBookingsForBooker(1,
                String.valueOf(State.ALL), Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingsForBooker(1,
                String.valueOf(State.CURRENT), Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingsForBooker(1,
                String.valueOf(State.PAST), Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingsForBooker(1,
                String.valueOf(State.FUTURE), Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingsForBooker(1,
                String.valueOf(State.WAITING), Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingsForBooker(1,
                String.valueOf(State.REJECTED), Pageable.ofSize(10)).size(), equalTo(1));
    }

    @Test
    @DisplayName("Проверка findAllBookingsForOwner для Booking")
    void shouldGetByOwner() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwnerId(anyInt(), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByIdDesc(anyInt(),
                any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStatusAndEndIsBeforeOrderByIdDesc(anyInt(),
                any(Status.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStatusInAndStartIsAfterOrderByIdDesc(anyInt(),
                any(), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdDesc(anyInt(),
                any(Status.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdDesc(anyInt(),
                any(Status.class))).thenReturn(List.of(booking));

        assertThat(bookingService.findAllBookingForOwner(1, String.valueOf(State.ALL),
                Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingForOwner(1, String.valueOf(State.CURRENT),
                Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingForOwner(1, String.valueOf(State.PAST),
                Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingForOwner(1, String.valueOf(State.FUTURE),
                Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingForOwner(1, String.valueOf(State.WAITING),
                Pageable.ofSize(10)).size(), equalTo(1));
        assertThat(bookingService.findAllBookingForOwner(1, String.valueOf(State.REJECTED),
                Pageable.ofSize(10)).size(), equalTo(1));
    }
}
