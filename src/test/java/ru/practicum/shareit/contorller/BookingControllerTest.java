package ru.practicum.shareit.contorller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.utility.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService service;

    private final User owner = User.builder()
            .id(1)
            .name("owner")
            .email("owner@user.com")
            .build();
    private final User booker = User.builder()
            .id(2)
            .name("booker")
            .email("booker@user.com")
            .build();
    private final Item item = Item.builder()
            .id(1).name("item")
            .description("description")
            .available(true)
            .ownerId(1)
            .build();
    private final BookingRequestDto bookingDto = BookingRequestDto.builder()
            .itemId(1)
            .userId(2)
            .status(Status.WAITING)
            .start(LocalDateTime.now().plusHours(10))
            .end(LocalDateTime.now().plusHours(100))
            .build();

    @Test
    @DisplayName("Тест POST запроса по эндпоинту /bookings")
    void testShouldSaveBookingWithStatusOk() throws Exception {
        when(service.save(any(BookingRequestDto.class))).thenReturn(
                BookingMapper.bookingToResponseDto(BookingMapper.requestDtoToBooking(bookingDto, item, booker))
        );
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.WAITING.name())));
    }

    @Test
    @DisplayName("Тест Get запроса по эндпоинту /bookings/id")
    void testShouldFindBookingByIdWithStatusOk() throws Exception {
        when(service.findBookingById(anyInt(), anyInt())).thenReturn(
                BookingMapper.bookingToResponseDto(BookingMapper.requestDtoToBooking(bookingDto, item, booker))
        );
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.WAITING.name())));
    }

    @Test
    @DisplayName("Тест PATCH запроса по эндпоинту /bookings/id")
    void testShouldUpdateBookingWithStatusOkOrNotFound() throws Exception {
        bookingDto.setStatus(Status.APPROVED);
        when(service.update(anyInt(), anyInt(), any(Status.class))).thenReturn(
                BookingMapper.bookingToResponseDto(BookingMapper.requestDtoToBooking(bookingDto, item, booker))
        );
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVED.name())));

        mvc.perform(patch("/bookings/1")
                        .param("approved", "false")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Status.APPROVED.name())));

        mvc.perform(patch("/bookings/1")
                        .param("approved", "error")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Тест GET запроса по эндпоинту /bookings арендатором")
    void testShouldFindAllBookingsForBookerWithStatusOk() throws Exception {
        when(service.findAllBookingsForBooker(anyInt(), String.valueOf(any(State.class)), any(Pageable.class))).thenReturn(
                List.of(BookingMapper.bookingToResponseDto(BookingMapper.requestDtoToBooking(bookingDto, item, booker)))
        );
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест GET запроса по эндпоинту /bookings владельцем")
    void testShouldFindAllBookingsForOwnerWithStatusOk() throws Exception {
        when(service.findAllBookingForOwner(anyInt(), String.valueOf(any(State.class)), any(Pageable.class))).thenReturn(
                List.of(BookingMapper.bookingToResponseDto(BookingMapper.requestDtoToBooking(bookingDto, item, booker)))
        );
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
