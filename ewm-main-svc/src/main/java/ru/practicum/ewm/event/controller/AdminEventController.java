package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventSearchDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;

import java.util.Collection;

/**
 * Контроллер для администрирования событий.
 * Позволяет искать и обновлять события с административными правами.
 */
@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    /**
     * Получает список событий по фильтру для администратора.
     * @param searchEventParams параметры поиска событий
     * @param request HTTP-запрос
     * @return коллекция событий, удовлетворяющих фильтру
     */
    @GetMapping
    public Collection<EventFullDto> findAllByAdmin(@Valid EventSearchDto searchEventParams,
                                                   HttpServletRequest request) {
        log.info("Запрос на получения событий с фильтром");
        Collection<EventFullDto> events = eventService.findAllByAdmin(searchEventParams, request);
        log.info("Отправлен ответ: {}", events);
        return events;
    }

    /**
     * Обновляет событие по id (админский доступ).
     * @param eventId идентификатор события
     * @param eventDto DTO с обновленными данными
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @RequestBody @Valid UpdateEventRequest eventDto) {
        log.info("Запрос на обновление события {} с телом {}", eventId, eventDto);
        EventFullDto event = eventService.updateEventByAdmin(eventId, eventDto);
        log.info("Обновленное событие: {}", event);
        return event;
    }
}