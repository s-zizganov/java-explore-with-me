package ru.practicum.ewm.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;

/**
 * Контроллер для управления пользователями (админ-панель).
 * Предоставляет эндпоинты для создания, удаления и получения пользователей.
 */
@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * Создать нового пользователя.
     *
     * @param userCreateDto DTO с данными нового пользователя
     * @return созданный пользователь
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Запрос на создание пользователя с данными: {}", userCreateDto);
        return userService.createUser(userCreateDto);
    }

    /**
     * Удалить пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя с id: {}", userId);
        userService.deleteUser(userId);
    }

    /**
     * Получить список пользователей с возможностью фильтрации по id и пагинацией.
     *
     * @param ids  список идентификаторов пользователей (опционально)
     * @param from индекс первого элемента (пагинация)
     * @param size размер страницы (пагинация)
     * @return список пользователей
     */
    @GetMapping
    public List<User> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка пользователей с параметрами: " +
                        "\n список ID: {}" +
                        "\n from: {}" +
                        "\n size: {}",
                ids, from, size);
        return userService.getAllUsers(ids, from, size);
    }
}
