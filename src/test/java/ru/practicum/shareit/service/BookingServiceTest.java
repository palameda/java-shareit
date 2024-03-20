package ru.practicum.shareit.contorller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImplementation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
            .start(LocalDateTime.now().plus(10, ChronoUnit.HOURS))
            .end(LocalDateTime.now().plus(100, ChronoUnit.HOURS))
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
    @DisplayName("Запись бронирования")
    void shouldSave() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        assertThat(bookingService.save(bookingDto).getBooker().getId(), equalTo(1));
        bookingDto.setStart(null);
        Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(bookingDto)
        );

        bookingDto.setStart(LocalDateTime.now().plus(1000, ChronoUnit.HOURS));
        Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(bookingDto)
        );

        assert item != null;
        item.setAvailable(false);
        Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(bookingDto)
        );
    }

    @Test
    @DisplayName("Обновление бронирования")
    void shouldUpdate() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Assertions.assertEquals(bookingService.update(1L, 1L, BookingStatus.REJECTED)
                .getId(), 1L);
        Assertions.assertThrows(ValidateException.class, () ->
                bookingService.update(1L, 1L, BookingStatus.REJECTED));

    }

    @Test
    @DisplayName("Просмотр бронирования")
    void shouldGet() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        Assertions.assertEquals(bookingService.getCertain(1L, 1L).getStatus(), BookingStatus.WAITING);

        Assertions.assertThrows(AccessException.class,
                () -> bookingService.getCertain(1L, 3L).getStatus());
    }

    @Test
    @DisplayName("Просмотр бронирований")
    void shouldAny() {
        when(bookingRepository.findByItem_IdAndStartIsBeforeOrderByEndDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItem_IdAndBooker_IdAndStatusAndEndIsBefore(anyLong(), anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(List.of(booking));


        Assertions.assertNotEquals(bookingService.getBookingByItemOld(1L), 0);
        Assertions.assertNotEquals(bookingService.getBookingByItemNew(1L, BookingStatus.WAITING), 0);
        Assertions.assertNotEquals(bookingService.getBookingByItemUserStatusOld(1L, 1L), 0);
    }

    @Test
    @DisplayName("Получаем бронирования по заявителю")
    void shouldGetByBooker() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBooker_Id(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBooker_IdAndStatusAndEndIsBeforeOrderByIdDesc(anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBooker_IdAndStatusInAndStartIsAfterOrderByIdDesc(anyLong(), any(),
                any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByIdDesc(anyLong(), any(BookingStatus.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByIdDesc(anyLong(), any(BookingStatus.class)))
                .thenReturn(List.of(booking));

        Assertions.assertEquals(bookingService.getBookingForBooker(1L,
                BookingStatusParam.ALL, Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForBooker(1L,
                BookingStatusParam.CURRENT, Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForBooker(1L,
                BookingStatusParam.PAST, Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForBooker(1L,
                BookingStatusParam.FUTURE, Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForBooker(1L,
                BookingStatusParam.WAITING, Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForBooker(1L,
                BookingStatusParam.REJECTED, Pageable.ofSize(10)).size(), 1);
    }

    @Test
    @DisplayName("Получение бронирований по владельцу")
    void shouldGetByOwner() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwner(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByIdDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerAndStatusAndEndIsBeforeOrderByIdDesc(anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerAndStatusInAndStartIsAfterOrderByIdDesc(
                anyLong(), any(), any(LocalDateTime.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerAndStatusOrderByIdDesc(anyLong(), any(BookingStatus.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerAndStatusOrderByIdDesc(anyLong(), any(BookingStatus.class)))
                .thenReturn(List.of(booking));

        Assertions.assertEquals(bookingService.getBookingForOwner(1L, BookingStatusParam.ALL,
                Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForOwner(1L, BookingStatusParam.CURRENT,
                Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForOwner(1L, BookingStatusParam.PAST,
                Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForOwner(1L, BookingStatusParam.FUTURE,
                Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForOwner(1L, BookingStatusParam.WAITING,
                Pageable.ofSize(10)).size(), 1);
        Assertions.assertEquals(bookingService.getBookingForOwner(1L, BookingStatusParam.REJECTED,
                Pageable.ofSize(10)).size(), 1);

    }


    @Test
    @DisplayName("Фиктивный тест на маппер")
    void test_cannot_instantiate() {
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            var constructor = BookingMapper.class.getDeclaredConstructor();
            Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
