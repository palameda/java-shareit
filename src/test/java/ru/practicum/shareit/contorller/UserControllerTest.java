package ru.practicum.shareit.UserTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    private static UserDto userRequest;
    private static UserDto userResponse;

    @BeforeAll
    public static void createUsers() {
        userRequest = UserDto.builder()
                .name("user")
                .email("user@user.com")
                .build();
        userResponse = UserDto.builder()
                .id(1)
                .name("user")
                .email("user@user.com")
                .build();
    }

    @Test
    @DisplayName("Тест GET запроса по эндпоинту /users")
    void testShouldFindAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(List.of(userResponse));
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест GET запроса по эндпоинту /users/{id}")
    public void testShouldFindUserById() throws Exception {
        when(userService.findById(1)).thenReturn(userResponse);
        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userRequest.getName())))
                .andExpect(jsonPath("$.email", is(userRequest.getEmail())));
    }

    @Test
    @DisplayName("Тест POST запроса по эндпоинту /users")
    void testShouldSaveUserOrReturnBadRequest() throws Exception {
        when(userService.saveUser(any(UserDto.class))).thenReturn(userResponse);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userRequest.getName())))
                .andExpect(jsonPath("$.email", is(userRequest.getEmail())));

        userRequest.setEmail("");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Тест POST запроса по эндпоинту /users (дублирование данных)")
    void testShouldReturnConflictCausedByDuplicateData() throws Exception {
        when(userService.saveUser(any(UserDto.class))).thenThrow(new DuplicateDataException("errorMessage"));
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Тест POST запроса по эндпоинту /users")
    void testShouldReturnBadRequest() throws Exception {
        when(userService.saveUser(any(UserDto.class))).thenThrow(new ValidationException("errorMessage"));
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Тест PATCH запроса по эндпоинту /users/id")
    void testShouldUpdateUserById() throws Exception {
        when(userService.updateUser(any(UserDto.class))).thenReturn(userResponse);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userRequest.getName())))
                .andExpect(jsonPath("$.email", is(userRequest.getEmail())));
    }

    @Test
    @DisplayName("Тест DELETE запроса по эндпоинту /users/id")
    void testShouldDeleteUserById() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
