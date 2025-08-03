package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.RequestStatusUpdateResult;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.EventState;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с заявками на участие в событиях.
 * Содержит бизнес-логику создания, отмены, получения и массового обновления заявок.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c ID " + userId + " не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие c ID " + eventId + " не найдено"));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Пользователь уже подал заявку на это событие.");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Инициатор не может подавать заявку на своё событие.");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии.");
        }

        if (event.getParticipantLimit() != 0 &&
                requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED) >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит участников.");
        }

        RequestStatus status = (!event.getRequestModeration() || event.getParticipantLimit() == 0)
                ? RequestStatus.CONFIRMED : RequestStatus.PENDING;

        Request request = new Request(1L, event, user, status, LocalDateTime.now());

        Request savedRequest = requestRepository.save(request);
        log.info("Создан запрос на участие с ID: {}", savedRequest.getId());
        return requestMapper.toRequestDto(savedRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c ID " + userId + " не найден"));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден."));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ForbiddenException("Можно отменить только собственный запрос.");
        }

        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return requestMapper.toRequestDto(savedRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c ID " + userId + " не найден"));

        return requestRepository.findByRequesterId(userId).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParticipationRequestDto> getParticipationRequestsForUserEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c ID " + userId + " не найден"));

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new ForbiddenException("Пользователь не инициатор события."));

        return requestRepository.findByEvent(event).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestStatusUpdateResult updateStatus(Long userId, Long eventId, RequestStatusUpdateRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c ID " + userId + " не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие c ID " + eventId + " не найдено"));

        List<Request> requests = requestRepository.findAllById(dto.getRequestIds());
        if (requests.isEmpty()) {
            return new RequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        }

        RequestStatus targetStatus = RequestStatus.valueOf(String.valueOf(dto.getStatus()));

        int participantLimit = event.getParticipantLimit();
        long confirmedCount = requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);

        if (targetStatus == RequestStatus.CONFIRMED && participantLimit != 0 && confirmedCount >= participantLimit) {
            throw new ConflictException("Достигнут лимит участников.");
        }

        RequestStatusUpdateResult result = new RequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());

        for (Request request : requests) {
            validateRequest(request, eventId);

            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Изменять можно только заявки в статусе PENDING.");
            }

            if (targetStatus == RequestStatus.CONFIRMED) {
                if (participantLimit != 0 && confirmedCount >= participantLimit) {
                    break; // Достигнут лимит участников
                }
                request.setStatus(RequestStatus.CONFIRMED);
                result.getConfirmedRequests().add(requestMapper.toRequestDto(request));
                confirmedCount++;
            } else if (targetStatus == RequestStatus.REJECTED) {
                request.setStatus(RequestStatus.REJECTED);
                result.getRejectedRequests().add(requestMapper.toRequestDto(request));
            }
        }

        requestRepository.saveAll(requests);
        return result;
    }

    /**
     * Валидация, что заявка относится к данному событию.
     *
     * @param request заявка на участие
     * @param eventId идентификатор события
     * @throws NotFoundException если заявка не относится к событию
     */
    private void validateRequest(Request request, Long eventId) {
        if (!request.getEvent().getId().equals(eventId)) {
            throw new NotFoundException("Запрос не относится к данному событию.");
        }
    }
}