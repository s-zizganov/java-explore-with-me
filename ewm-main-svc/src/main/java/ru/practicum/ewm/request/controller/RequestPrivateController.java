package ru.practicum.ewm.request.controller;


import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.RequestStatusUpdateResult;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

/**
 * Контроллер для управления заявками на участие пользователя в событиях.
 * Предоставляет эндпоинты для создания, отмены, получения и обновления заявок.
 */
@Slf4j
@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class RequestPrivateController {

    private final RequestService eventRequestService;

    /**
     * Создать заявку на участие пользователя в событии.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return созданная заявка на участие
     */
    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable @Positive Long userId,
                                          @RequestParam @Positive Long eventId) {
        log.info("Запрос на создание заявки на участие пользователя {} в событии {}", userId, eventId);
        ParticipationRequestDto createdRequest = eventRequestService.create(userId, eventId);
        log.info("Создана заявка на участие: {}", createdRequest);
        return createdRequest;
    }

    /**
     * Отменить заявку на участие пользователя в событии.
     *
     * @param userId    идентификатор пользователя
     * @param requestId идентификатор заявки
     * @return отменённая заявка на участие
     */
    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive Long userId,
                                          @PathVariable @Positive Long requestId) {
        log.info("Отмена заявки {} на участие пользователя в событии {}", userId, requestId);
        ParticipationRequestDto cancelledRequest = eventRequestService.cancelRequest(userId, requestId);
        log.info("Заявка отменена: {}", cancelledRequest);
        return cancelledRequest;
    }

    /**
     * Получить список заявок пользователя на участие в событиях.
     *
     * @param userId идентификатор пользователя
     * @return список заявок на участие
     */
    @GetMapping("/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable @Positive Long userId) {
        log.info("Запрос на получение списка заявок на участие в событии пользователя {}", userId);
        List<ParticipationRequestDto> requests = eventRequestService.getParticipationRequests(userId);
        log.info("Получены заявки на участие: {}", requests);
        return requests;
    }

    /**
     * Получить список заявок на участие в конкретном событии пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return список заявок на участие в событии
     */
    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsForUserEvent(@PathVariable @Positive Long userId,
                                                                              @PathVariable @Positive Long eventId) {
        log.info("Запрос на получение списка заявок на участие в событии {} пользователя {}", userId, eventId);
        List<ParticipationRequestDto> requests = eventRequestService.getParticipationRequestsForUserEvent(userId, eventId);
        log.info("Получены заявки на участие в событии: {}", requests);
        return requests;
    }

    /**
     * Изменить статусы заявок на участие в событии пользователя.
     *
     * @param userId    идентификатор пользователя
     * @param eventId   идентификатор события
     * @param requestDto DTO с информацией об изменении статусов заявок
     * @return результат обновления статусов заявок
     */
    @PatchMapping("/events/{eventId}/requests")
    public RequestStatusUpdateResult updateStatus(@PathVariable @Positive Long userId,
                                                  @PathVariable @Positive Long eventId,
                                                  @RequestBody RequestStatusUpdateRequest requestDto) {
        log.info("Запрос на изменение статуса заявок события {} пользователя {} с телом {}", userId, eventId, requestDto);
        RequestStatusUpdateResult updateResult = eventRequestService.updateStatus(userId, eventId, requestDto);
        log.info("Обновлен статус заявок: {}", updateResult);
        return updateResult;
    }
}