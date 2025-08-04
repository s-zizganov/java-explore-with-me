package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.RequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Сервисный интерфейс для работы с заявками на участие в событиях.
 * Описывает основные операции создания, отмены, получения и обновления заявок.
 */
public interface RequestService {

    /**
     * Создать заявку на участие пользователя в событии.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return созданная заявка на участие
     */
    ParticipationRequestDto create(Long userId, Long eventId);

    /**
     * Отменить заявку пользователя на участие в событии.
     *
     * @param userId    идентификатор пользователя
     * @param requestId идентификатор заявки
     * @return отменённая заявка на участие
     */
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    /**
     * Получить список заявок пользователя на участие в событиях.
     *
     * @param userId идентификатор пользователя
     * @return список заявок на участие
     */
    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    /**
     * Получить список заявок на участие в конкретном событии пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return список заявок на участие в событии
     */
    List<ParticipationRequestDto> getParticipationRequestsForUserEvent(Long userId, Long eventId);

    /**
     * Массово обновить статусы заявок на участие в событии пользователя.
     *
     * @param userId    идентификатор пользователя
     * @param eventId   идентификатор события
     * @param dto       DTO с информацией об изменении статусов заявок
     * @return результат обновления статусов заявок
     */
    RequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                           RequestStatusUpdateRequest dto);
}