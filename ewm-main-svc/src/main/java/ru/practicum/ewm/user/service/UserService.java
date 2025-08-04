package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

/**
 * Сервисный интерфейс для работы с пользователями.
 * Описывает основные операции создания, удаления и получения пользователей.
 */
public interface UserService {

    /**
     * Создать нового пользователя.
     *
     * @param userCreateDto DTO с данными нового пользователя
     * @return созданный пользователь
     */
    User createUser(UserCreateDto userCreateDto);

    /**
     * Удалить пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     */
    void deleteUser(Long id);

    /**
     * Получить список пользователей с возможностью фильтрации по id и пагинацией.
     *
     * @param ids  список идентификаторов пользователей (опционально)
     * @param from индекс первого элемента (пагинация)
     * @param size размер страницы (пагинация)
     * @return список пользователей
     */
    List<User> getAllUsers(List<Long> ids, int from, int size);
}
