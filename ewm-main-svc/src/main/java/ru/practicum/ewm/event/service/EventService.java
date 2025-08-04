package ru.practicum.ewm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.event.dto.*;

import java.util.Collection;

/**
 * Сервисный интерфейс для работы с событиями.
 * Описывает основные операции создания, обновления, поиска и получения событий.
 */
public interface EventService {
    /**
     * Создать новое событие для пользователя.
     *
     * @param userId         идентификатор пользователя
     * @param eventCreateDto DTO с данными нового события
     * @return полная информация о созданном событии
     */
    EventFullDto create(Long userId, EventCreateDto eventCreateDto);

    /**
     * Обновить событие администратором.
     *
     * @param eventId      идентификатор события
     * @param adminRequest DTO с изменениями
     * @return полная информация об обновлённом событии
     */
    EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest adminRequest);

    /**
     * Обновить событие пользователем (инициатором).
     *
     * @param userId           идентификатор пользователя
     * @param eventId          идентификатор события
     * @param eventUserRequest DTO с изменениями
     * @return полная информация об обновлённом событии
     */
    EventFullDto updateEventByPrivate(Long userId, Long eventId, UpdateEventRequest eventUserRequest);

    /**
     * Получить событие пользователя по идентификатору.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return полная информация о событии
     */
    EventFullDto getEventOfUser(Long userId, Long eventId);

    /**
     * Получить список событий для публичного доступа с фильтрацией.
     *
     * @param searchDto параметры поиска
     * @param request   HTTP-запрос
     * @return коллекция краткой информации о событиях
     */
    Collection<EventShortDto> findAllByPublic(EventSearchDto searchDto, HttpServletRequest request);

    /**
     * Получить список событий пользователя (инициатора).
     *
     * @param userId  идентификатор пользователя
     * @param from    индекс первого элемента
     * @param size    размер страницы
     * @param request HTTP-запрос
     * @return коллекция краткой информации о событиях
     */
    Collection<EventShortDto> findAllByPrivate(Long userId, Integer from, Integer size, HttpServletRequest request);

    /**
     * Получить список событий для администратора с фильтрацией.
     *
     * @param searchDto параметры поиска
     * @param request   HTTP-запрос
     * @return коллекция полной информации о событиях
     */
    Collection<EventFullDto> findAllByAdmin(EventSearchDto searchDto, HttpServletRequest request);

    /**
     * Получить полную информацию о событии по идентификатору (только опубликованные).
     *
     * @param eventId идентификатор события
     * @param request HTTP-запрос
     * @return полная информация о событии
     */
    EventFullDto findEventById(Long eventId, HttpServletRequest request);

}