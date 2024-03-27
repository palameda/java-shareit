package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.db.ItemRequestDbRepository;
import ru.practicum.shareit.request.utility.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.utility.UserMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemRequestServiceImplementation implements ItemRequestService {
   private final UserService userService;
   private final ItemService itemService;
   private final ItemRequestDbRepository itemRequestRepository;

    @Override
    public ItemRequest findRequestById(Integer itemRequestId, Integer userId) {
        userService.findById(userId);

        return itemRequestRepository.findById(itemRequestId).orElseThrow(
                () -> new NotFoundException("Запрос с id " + itemRequestId + "не зарегистрирован в системе")
        );
    }

    @Override
    public ItemResponseDto findById(Integer itemRequestId, Integer userId) {
        userService.findById(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(
                () -> new NotFoundException("Запрос с id " + itemRequestId + "не зарегистрирован в системе")
        );
        log.info("Сервис: получение запроса на добаление вещи по id {} пользователем с id {}", itemRequestId, userId);
        return ItemRequestMapper.itemRequestToResponseDto(
                itemRequest, itemService.findRequestedItems(itemRequest.getId())
        );
    }

    @Override
    public List<ItemResponseDto> findAllByAuthor(Integer userId) {
        userService.findById(userId);
        log.info("Сервис: получение запросов на добаление вещей автором с id {}", userId);
        return itemRequestRepository.findAllByAuthorIdOrderByIdDesc(userId).stream()
                .map(item -> ItemRequestMapper.itemRequestToResponseDto(
                        item, itemService.findRequestedItems(item.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> findAll(Integer userId, Pageable pageable) {
        return itemRequestRepository.findAllByAuthorIdNot(userId, pageable).stream()
                .map(item -> ItemRequestMapper.itemRequestToResponseDto(
                        item, itemService.findRequestedItems(item.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ItemResponseDto save(ItemRequestDto requestDto) {
        User user = UserMapper.dtoToUser(userService.findById(requestDto.getUserId()));
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.requestDtoToItemRequest(requestDto, user));
        return ItemRequestMapper.itemRequestToResponseDto(
                itemRequest, itemService.findRequestedItems(itemRequest.getId())
        );
    }
}
