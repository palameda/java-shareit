package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.db.BookingDbRepository;
import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.comment.repository.db.CommentDbRepository;
import ru.practicum.shareit.comment.utility.CommentMapper;
import ru.practicum.shareit.exception.DenialOfAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemDbRepository;
import ru.practicum.shareit.item.utility.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс ItemServiceImplementation реализует методы интерфейса {@link ItemService}.
 * Описывает логику работы приложения c сущностью {@link Item}.
 * @see ItemDbRepository
 * @see UserDbRepository
 * @see ItemMapper
 * @see ItemDto
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImplementation implements ItemService {
    private final ItemDbRepository itemRepository;
    private final UserDbRepository userRepository;
    private final BookingDbRepository bookingRepository;
    private final CommentDbRepository commentRepository;

    @Override
    public List<ItemDto> findAll(Integer userId) {
        log.info("Сервис: обработка запроска на получение списка всех вещей владельца с id {}", userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::itemToDto)
                .peek(itemDto -> {
                    bookingRepository.findByItemIdAndStartIsBeforeOrderByEndDesc(
                                    itemDto.getId(), LocalDateTime.now()).stream()
                            .findFirst()
                            .ifPresent(lastBooking -> itemDto.setLastBooking(ItemMapper.itemToBookingReference(lastBooking)));
                    bookingRepository.findByItemIdAndStartIsAfterAndStatusOrderByStartAsc(
                                    itemDto.getId(), LocalDateTime.now(), Status.APPROVED).stream()
                            .findFirst()
                            .ifPresent(nextBooking -> itemDto.setNextBooking(ItemMapper.itemToBookingReference(nextBooking)));
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Integer itemId, Integer userId) {
        log.info("Сервис: обработка запроса на получение вещи с id {}", itemId);
        ItemDto itemDto = ItemMapper.itemToDto(itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id " + itemId + " не зарегистрирована в системе"))
        );
        if (Objects.equals(itemDto.getOwnerId(), userId)) {
            bookingRepository.findByItemIdAndStartIsBeforeOrderByEndDesc(
                            itemId, LocalDateTime.now()).stream()
                    .findFirst()
                    .ifPresent(lastBooking -> itemDto.setLastBooking(ItemMapper.itemToBookingReference(lastBooking)));
            bookingRepository.findByItemIdAndStartIsAfterAndStatusOrderByStartAsc(
                    itemDto.getId(), LocalDateTime.now(), Status.APPROVED).stream()
                    .findFirst()
                    .ifPresent(nextBooking -> itemDto.setNextBooking(ItemMapper.itemToBookingReference(nextBooking)));
        }
        itemDto.setComments(commentRepository.findAllByItemIdOrderById(itemId).stream()
                .map(CommentMapper::commentToResponse)
                .collect(Collectors.toList())
        );
        return itemDto;
    }

    @Transactional
    @Override
    public ItemDto saveItem(ItemDto itemDto, Integer userId) {
        log.info("Сервис: обработка запроса на сохранение вещи {} её владельцем с id {}", itemDto.getName(), userId);
        checkUser(userId);
        Item savedItem = ItemMapper.dtoToItem(itemDto);
        //savedItem.setOwnerId(userId);
        return ItemMapper.itemToDto(itemRepository.save(savedItem));
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, Integer userId) {
        log.info("Сервис: обработка запроса на изменение данных вещи {} пользователем с id {}", itemDto.getName(), userId);
        checkUser(userId);
        checkPossession(itemDto, userId);
        ItemDto storedItem = findById(itemDto.getId(), userId);
        if (itemDto.getName() != null) {
            storedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            storedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            storedItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.itemToDto(
                itemRepository.save(ItemMapper.dtoToItem(storedItem))
        );
    }

    @Transactional
    @Override
    public void deleteItem(Integer itemId, Integer userId) {
        log.info("Сервис: обработка запроса на удаление вещи {} пользователем с id {}", itemId, userId);
        checkUser(userId);
        ItemDto checkItem = ItemMapper.itemToDto(itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id " + itemId + " не зарегистрирована в системе")
        ));
        checkPossession(checkItem, userId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> seekItem(String searchQuery) {
        log.info("Сервис: обработка поискового запроса {}", searchQuery);
        return itemRepository.findByNameOrDescriptionAndAvailable(searchQuery).stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseComment addComment(RequestComment comment) {
        User author = checkUser(comment.getUserId());
        Item item = checkItem(comment.getItemId());
        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndStatusAndEndIsBefore(
                comment.getItemId(), comment.getUserId(), Status.APPROVED, LocalDateTime.now()
        );
        if (bookings.isEmpty()) {
            throw new ValidationException("Пользователя с id " + comment.getUserId() + " не использовал вещи");
        }
        return CommentMapper.commentToResponse(
                commentRepository.save(CommentMapper.requestToComment(comment, item, author))
        );
    }

    /**
     * Приватный метод checkPossession проверяет является ли пользователь владельцем вещи.
     * Если пользователь не является владельцем вещи, то возникает исключение {@link DenialOfAccessException}
     * @param itemDto данные вещи для проверки
     * @param ownerId идентификатор пользователя для проверки
     */
    private void checkPossession(ItemDto itemDto, Integer ownerId) {
        log.info("Сервис: проверка принадлежности вещи {} пользователю с id {}", itemDto.getName(), ownerId);
        Item savedItem = checkItem(itemDto.getId());
        if (!Objects.equals(savedItem.getOwnerId(), ownerId)) {
            throw new DenialOfAccessException(
                    "Отказ в доступе. Пользователь с id " + ownerId + " не является владельцем вещи " + itemDto.getName()
            );
        }
    }

    private User checkUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не зарегистрирован в системе")
        );
    }

    private Item checkItem(Integer itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь c id " + itemId + " не зарегистрирована в системе")
        );
    }
}
