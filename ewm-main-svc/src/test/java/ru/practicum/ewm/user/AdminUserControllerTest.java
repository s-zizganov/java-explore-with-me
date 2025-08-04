package ru.practicum.ewm.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.controller.AdminUserController;
import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AdminUserController.class)
@DisplayName("Тестирование AdminUserController")
public class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @DisplayName("Успешное создание игрока")
    @Test
    void createUser_correctUser_shouldReturnCreated() throws Exception {
        UserCreateDto dto = new UserCreateDto("User Name", "user@mail.ru");

        User createdUser = new User(1L, "User Name", "user@mail.ru");

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@mail.ru"));
    }

    @DisplayName("Попытка повторно создать пользователя")
    @Test
    void createUser_existUser_shouldReturnConflict() throws Exception {
        UserCreateDto dto = new UserCreateDto("User Name", "user@mail.ru");

        when(userService.createUser(any(UserCreateDto.class)))
                .thenThrow(new ConflictException("Пользователь с user@mail.ru уже существует"));

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @DisplayName("Некорректная почта")
    @Test
    void createUser_invalidEmailUser_shouldReturnBadRequest() throws Exception {
        UserCreateDto dto = new UserCreateDto("User Name", "user-mail.ru");

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Успешное удаление пользователя")
    @Test
    void deleteUser_correctId_shouldReturnNoContent() throws Exception {
       doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Попытка удалить несуществующего пользователя")
    @Test
    void deleteUser_doesNotExistId_shouldReturnNotFound() throws Exception {
        doThrow(new NotFoundException("Пользователя не существует")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Получение всех пользователей")
    @Test
    void getAllUsers_nullIdList_shouldReturnOk() throws Exception {
        List<User> users = List.of(
                new User(1L, "John",  "user1@example.com"),
                new User(2L, "Jane",  "user2@example.com")
        );

        when(userService.getAllUsers(any(), anyInt(), anyInt())).thenReturn(users);

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @DisplayName("Получение пользователей по Id")
    @Test
    void getAllUsers_allParams_shouldReturnOkWithFilter() throws Exception {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<User> users = List.of(
                new User(1L, "John",  "user1@example.com")
        );

        when(userService.getAllUsers(anyList(), anyInt(), anyInt())).thenReturn(users);

        mockMvc.perform(get("/admin/users").param("ids", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @DisplayName("Получение пользователей. Некорректное значение from")
    @Test
    void getAllUsers_fromIsNegative_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/admin/users").param("from", "-1"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Получение пользователей. Некорректное значение size")
    @Test
    void getAllUsers_sizeIsZeroOrNegative_shouldReturnBadRequest() throws Exception {
       mockMvc.perform(get("/admin/users").param("size", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/users").param("size", "-5"))
                .andExpect(status().isBadRequest());
    }

}
