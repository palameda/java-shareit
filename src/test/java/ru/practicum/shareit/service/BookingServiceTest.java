package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.booking.service.BookingServiceImplementation;
import ru.practicum.shareit.booking.utility.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private UserDbRepository userRepository;
    @Mock
    private ItemDbRepository itemRepository;
    @Mock
    private BookingDbRepository bookingRepository;
    @InjectMocks
    private BookingServiceImplementation bookingService;

    private User owner;
    private User booker;
    private Item item;
    private Item saveItem;
    private Booking booking;

    @BeforeEach
    void setup() {
        owner = User.builder()
                .id(1)
                .name("owner")
                .email("owner@user.com")
                .build();
        booker = User.builder()
                .id(2)
                .name("booker")
                .email("booker@user.com")
                .build();
        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .ownerId(1)
                .available(true)
                .build();
        saveItem = Item.builder()
                .id(2)
                .name("testItem")
                .description("description")
                .ownerId(2)
                .available(true)
                .build();

        booking = new Booking(1,
                LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                item, owner, Status.WAITING
        );

        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(2));
        saveItem.setAvailable(true);
    }

    @Test
    @DisplayName("Тест метода save. Сохраняет бронирование, когда бронирование не null")
    void save_whenBookingIsValid_thenReturnBookingResponseDto() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(saveItem));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingResponseDto bookingResponse = bookingService.save(BookingMapper.bookingToRequestDto(booking));
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        Assertions.assertEquals(bookingResponse.getBooker(), booking.getBooker());
    }

    @Test
    @DisplayName("Тест метода save. Выбравсывает ValidationException, когда start == null")
    void save_whenBookingStartIsNull_thenValidationExceptionThrown() {
        String errorMessage = "Ошибка в данных даты начала или даты окончания бронирования";
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(saveItem));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        booking.setStart(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбравсывает ValidationException, когда end == null")
    void save_whenBookingEndIsNull_thenValidationExceptionThrown() {
        String errorMessage = "Ошибка в данных даты начала или даты окончания бронирования";
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(saveItem));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        booking.setStart(LocalDateTime.now());
        booking.setEnd(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбравсывает ValidationException, когда start == null и end == null")
    void save_whenBookingStartAndEndAreNull_thenValidationExceptionThrown() {
        String errorMessage = "Ошибка в данных даты начала или даты окончания бронирования";
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(saveItem));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        booking.setStart(null);
        booking.setEnd(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбравсывает ValidationException, когда start == end")
    void save_whenBookingStartEqualToEnd_thenValidationExceptionThrown() {
        String errorMessage = "Даты начала и окончания бронирования не должны совпадать";
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(saveItem));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart());
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбравсывает ValidationException, когда start > end")
    void save_whenBookingStartIsAfterEnd_thenValidationExceptionThrown() {
        String errorMessage = "Даты начала и окончания бронирования не должны совпадать";
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(saveItem));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        booking.setStart(LocalDateTime.now());
        booking.setEnd(booking.getStart());
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбравсывает ValidationException, когда available == false")
    void save_whenBookingAvailableIsFalse_thenValidationExceptionThrown() {
        String errorMessage = "Бронь вещи " + BookingMapper.bookingToRequestDto(booking).toString() + " недоступна";
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(saveItem));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));

        saveItem.setAvailable(false);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбравсывает NotFoundException, когда вещь бронирует её владелец")
    void save_whenBookingByOwner_thenNotFoundExceptionThrown() {
        String errorMessage = "Владелец не может бронировать свои вещи";
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбрасывает NotFoundException, когда вещь не зарегистрирована")
    void save_whenBookingItemDoesNotExist_thenNotFoundExceptionThrown() {
        when(itemRepository.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        verify(itemRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода save. Выбрасывает NotFoundException, когда пользователь не зарегистрирован")
    void save_whenBookingUserDoesNotExist_thenNotFoundExceptionThrown() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.save(BookingMapper.bookingToRequestDto(booking))
        );
        verify(itemRepository, times(1)).findById(anyInt());
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода findBookingById. Возвращает Booking")
    void findBookingById_whenInvoked_thenReturnBookingResponseDto() {
        Integer bookingId = 1;
        Integer bookerId = 1;
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        BookingResponseDto actualBooking = bookingService.findBookingById(bookingId, bookerId);
        Assertions.assertEquals(actualBooking.toString(), BookingMapper.bookingToResponseDto(booking).toString());
    }

    @Test
    @DisplayName("Тест метода findBookingById. Выбравсывает NotFoundException, когда Booking не существует")
    void findBookingById_whenBookingDoesNotExist_thenNotFoundExceptionThrown() {
        Integer bookingId = 1;
        Integer bookerId = 1;
        when(bookingRepository.findById(anyInt())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.findBookingById(bookingId, bookerId)
        );
        verify(bookingRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Тест метода findBookingById. Выбравсывает NotFoundException, когда пользователь не может просматривать бронь")
    void findBookingById_whenBookerIsNotAllowed_thenNotFoundExceptionThrown() {
        Integer bookingId = 1;
        Integer bookerId = 2;
        String errorMessage = "Пользователь с id " + bookerId + " не может выполнять просмотр бронироввания с id " +
                bookingId;
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));
        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.findBookingById(bookingId, bookerId)
        );
        Assertions.assertEquals(exception.getMessage(), errorMessage);
    }

    @Test
    @DisplayName("Тест findAllBookingForOwner со state == All. Возвращает список броней на вещи владельца")
    void findAllBookingForOwner_whenStateIsAll_thenReturnListOfBookings() {
        Integer ownerId = 1;
        int size = 1;
        String state = "ALL";
        Pageable page = Pageable.ofSize(size);
        BookingResponseDto bookingResponse = BookingMapper.bookingToResponseDto(booking);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerId(ownerId, page)).thenReturn(List.of(booking));
        List<BookingResponseDto> ownerItems = bookingService.findAllBookingForOwner(ownerId, state, page);
        Assertions.assertFalse(ownerItems.isEmpty());
        Assertions.assertEquals(ownerItems.get(0).toString(), bookingResponse.toString());
    }
}
