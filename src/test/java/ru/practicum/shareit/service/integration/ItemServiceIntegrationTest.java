package ru.practicum.shareit.service.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.db.CommentDbRepository;
import ru.practicum.shareit.exception.DenialOfAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImplementation;
import ru.practicum.shareit.request.repository.db.ItemRequestDbRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceIntegrationTest {
    private ItemDbRepository itemRepository;
    private CommentDbRepository commentRepository;
    private ItemRequestDbRepository itemRequestRepository;
    private UserDbRepository userRepository;
    private BookingDbRepository bookingRepository;
    private ItemService itemService;

    private final UserDto userDto = UserDto.builder()
            .id(1)
            .name("user")
            .email("user@user.com")
            .build();
    private final User user = User.builder()
            .id(1)
            .name("user")
            .email("user@user.com")
            .build();
    private final Item item = Item.builder()
            .id(1)
            .name("item")
            .description("description")
            .available(true)
            .ownerId(1)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1)
            .name("item")
            .description("description")
            .available(false)
            .ownerId(1)
            .build();
    private final RequestComment requestComment = new RequestComment(1, 1, "comment");
    private final ResponseComment responseComment = new ResponseComment(1, "comment", "author",
            LocalDateTime.now());
    private final Comment comment = new Comment(1, item, user, "comment", LocalDateTime.now());

    @BeforeEach
    void setup() {
        itemRepository = mock(ItemDbRepository.class);
        commentRepository = mock(CommentDbRepository.class);
        itemRequestRepository = mock(ItemRequestDbRepository.class);
        userRepository = mock(UserDbRepository.class);
        bookingRepository = mock(BookingDbRepository.class);

        itemService = new ItemServiceImplementation(itemRepository, userRepository, bookingRepository, commentRepository,
                itemRequestRepository);
    }

//    @Test
//    @DisplayName("Проверка saveItem для Item")
//    void testShouldSaveItem() {
//        itemDto.setRequestId(1);
//        when(itemRequestRepository.findById(anyInt())).thenReturn(
//                Optional.of(new ItemRequest(1, user, "description", LocalDateTime.now())));
//        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.dtoToItem(
//                itemDto, new ItemRequest(1, user, "description", LocalDateTime.now())));
//        Assertions.assertEquals(itemService.saveItem(itemDto, 1).getRequestId(), 1);
////        Assertions.assertEquals(itemService.findById(1, 1).getOwnerId(), 1);
////        Assertions.assertEquals(itemService.findById(1, 1).getLastBooking().getId(), 1);
////        Assertions.assertThrows(
////                NotFoundException.class,
////                () -> itemService.findById(2, 1)
////        );
//    }

    @Test
    @DisplayName("Проверка метода updateItem для Item")
    void testShouldUpdateItemOrThrowDenialOfAccessException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        assert item != null;
        item.setDescription(null);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        Assertions.assertNull(itemService.updateItem(itemDto, 1).getDescription());
        Assertions.assertThrows(
                DenialOfAccessException.class, () -> itemService.updateItem(itemDto, 2)
        );
    }

    @Test
    @DisplayName("Проверка метода deleteItem для Item")
    void testShouldDeleteItemOrThrowDenialOfAccessException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        Assertions.assertThrows(
                DenialOfAccessException.class,
                () -> itemService.deleteItem(1, 2)
        );
        Assertions.assertDoesNotThrow(
                () -> itemService.deleteItem(1, 1)
        );
    }

//    @Test
//    @DisplayName("Проверка метода seekItem для Item")
//    void testShouldFindItemsUsingSearchString() {
//        when(itemService.seekItem("item")).thenReturn(
//                Stream.of(item).map(ItemMapper::itemToDto).collect(Collectors.toList())
//        );
//        when(itemRepository.findByNameOrDescriptionAndAvailable("")).thenReturn(List.of());
//        Assertions.assertEquals(itemService.seekItem("item").get(0).getId(), 1);
//        Assertions.assertEquals(itemService.seekItem("").size(), 0);
//    }

//    @Test
//    @DisplayName("Проверка метода save для Comment")
//    void testShouldAddCommentToBookedItem() {
//        when(itemRepository.findById(1)).thenReturn(Optional.ofNullable(item));
////        when(bookingRepository.findAllByItemIdAndBookerIdAndStatusAndEndIsBefore()
////                getBookingByItemUserStatusOld(anyInt(), anyInt())).thenReturn(List.of(new Booking()));
//        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
//
//        requestComment.setItemId(2);
//        Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(requestComment));
//        Assertions.assertEquals(itemService.addComment(requestComment).getText(), "comment");
//    }

    @Test
    @DisplayName("Проверка метода findAllByOwnerId для Item")
    void testShouldFindAllItemsByOwner() {
        when(itemRepository.findAllByOwnerId(anyInt())).thenReturn(List.of(item));
        Assertions.assertEquals(itemService.findAll(1).get(0).getDescription(), item.getDescription());
    }
}
