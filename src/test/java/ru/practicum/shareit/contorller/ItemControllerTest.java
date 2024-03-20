package ru.practicum.shareit.contorller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.RequestComment;
import ru.practicum.shareit.comment.dto.ResponseComment;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService service;

    private final ItemDto itemIn = ItemDto.builder()
            .name("item")
            .description("description")
            .available(true)
            .ownerId(1)
            .build();

    private final RequestComment commentRequest = new RequestComment(1, 1, "comment");
    private final ResponseComment commentResponse =
            new ResponseComment(1, "comment", "author", LocalDateTime.now());

    private final ItemDto itemResponse = ItemDto.builder()
            .name("item")
            .description("description")
            .available(true)
            .ownerId(1)
            .comments(List.of(commentResponse))
            .build();

    @Test
    @DisplayName("Тест POST запроса по эндпоинту /items")
    void testShouldSaveItemWithStatusOK() throws Exception {
        when(service.saveItem(any(ItemDto.class), anyInt())).thenReturn(itemResponse);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemIn.getDescription())));
    }

    @Test
    @DisplayName("Тест GET запроса по эндпоинту /items/id")
    void testShouldFindItemByIdWithStatusOk() throws Exception {
        when(service.findById(anyInt(), anyInt())).thenReturn(itemResponse);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemIn.getDescription())));
    }

    @Test
    @DisplayName("Тест PATCH запроса по эндпоинту /items/id")
    void testShouldUpdateItemWithStatusOK() throws Exception {
        when(service.updateItem(any(ItemDto.class), anyInt())).thenReturn(itemResponse);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("item")));
    }

    @Test
    @DisplayName("Тест DELETE запроса по эндпоинту /items/id")
    void testShouldDeleteItemWithStatusOk() throws Exception {
        mvc.perform(delete("/items/10")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест GET запроса по эндпоинту /items")
    void testShouldFindAllItemsWithStatusOK() throws Exception {
        when(service.findAll(anyInt())).thenReturn(List.of(itemResponse));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест GET запроса по эндпоинту /items/search")
    void testShouldFindItemsUsingSearchStringWithStatusOk() throws Exception {
        when(service.seekItem(anyString())).thenReturn(List.of(itemResponse));
        mvc.perform(get("/items/search")
                        .param("text", "item")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест POST запроса по эндпоинту /items/id/comment")
    void testShouldAddCommentWithStatusOk() throws Exception {
        when(service.addComment(any(RequestComment.class))).thenReturn(commentResponse);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentRequest))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
