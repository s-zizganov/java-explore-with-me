package ru.practicum.ewm.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование UserServiceImp")
public class UserServiceImpTest {

    @Autowired
    UserService userService;

    @DisplayName("Проверка, что нельзя создать двух пользователей с одним email")
    @Test
    void createUser_DuplicateUser_returnConflictException() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");
        userService.createUser(userCreateDto);
        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.createUser(userCreateDto),
                "Ожидается, что будет выброшено исключение ConflictException"
        );

        assertTrue(exception.getMessage().contains("Пользователь с email " + userCreateDto.getEmail() + " уже существует"));
    }

    @DisplayName("Создание нового пользователя")
    @Test
    void createUser_UniqueUser_returnUser() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");

        User createdUser = userService.createUser(userCreateDto);

        assertNotNull(createdUser.getId());
        assertEquals(userCreateDto.getEmail(), createdUser.getEmail());
        assertEquals(userCreateDto.getName(), createdUser.getName());

    }

    @DisplayName("Удаление несуществующего пользователя")
    @Test
    void deleteUser_NotExistUser_returnNotFoundException() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.deleteUser(1L),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );
    }

    @DisplayName("Удаление пользователя")
    @Test
    void deleteUser_CorrectUser_returnNothing() {
        userService.createUser(new UserCreateDto("User name", "user@email.ru"));
        userService.createUser(new UserCreateDto("User2 name", "user2@email.ru"));

        userService.deleteUser(1L);

        assertEquals(userService.getAllUsers(null, 0, 10).size(), 1);

    }
}
