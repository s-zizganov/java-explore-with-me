package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventSearchDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import java.util.Collection;

/**
 * Публичный контроллер для работы с событиями.
 * Предоставляет эндпоинты для поиска и получения информации о событиях.
 */
@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    /**
     * Получить список событий с возможностью фильтрации.
     *
     * @param searchEventParams параметры фильтрации событий
     * @param request           HTTP-запрос
     * @return коллекция краткой информации о событиях
     */
    @GetMapping
    public Collection<EventShortDto> findAllByPublic(@Valid EventSearchDto searchEventParams,
                                                     HttpServletRequest request) {
        log.info("Запрос на получения событий с фильтром");
        Collection<EventShortDto> events = eventService.findAllByPublic(searchEventParams, request);
        log.info("Отправлен ответ с телом: {}", events);
        return events;
    }

    /**
     * Получить подробную информацию о событии по его идентификатору.
     *
     * @param eventId идентификатор события
     * @param request HTTP-запрос
     * @return подробная информация о событии
     */
    @GetMapping("/{eventId}")
    public EventFullDto findEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Запрос события ", eventId);
        EventFullDto event = eventService.findEventById(eventId, request);
        log.info("Отправлен ответ телом: {}", eventId, event);
        return event;
    }
}