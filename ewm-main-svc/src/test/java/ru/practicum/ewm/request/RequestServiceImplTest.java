package ru.practicum.ewm.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.RequestStatusUpdateResult;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.EventState;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование RequestServiceImpl")
public class RequestServiceImplTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RequestRepository requestRepository;

    private Long userId;
    private Long eventId;

    @BeforeEach
    void setUp() {
        User user1 = new User(null, "Test User 1", "user1@example.com");
        user1 = userRepository.save(user1);
        userId = user1.getId();

        Event event = new Event();
        event.setInitiator(user1);
        event.setState(EventState.PUBLISHED);
        event.setRequestModeration(true);
        event.setParticipantLimit(2);
        event = eventRepository.save(event);
        eventId = event.getId();
    }

    @AfterEach
    void tearDown() {
        requestRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Создание запроса: пользователь не найден")
    void create_UserNotFound_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> requestService.create(999L, eventId));
    }

    @Test
    @DisplayName("Создание запроса: событие не найдено")
    void create_EventNotFound_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> requestService.create(userId, 999L));
    }

    @Test
    @DisplayName("Создание запроса: уже есть заявка")
    void create_RequestAlreadyExists_throwsConflictException() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);
        Long secondUserId = user2.getId();

        requestService.create(secondUserId, eventId);

        assertThrows(ConflictException.class,
                () -> requestService.create(secondUserId, eventId));
    }

    @Test
    @DisplayName("Создание запроса: пользователь — инициатор")
    void create_UserIsInitiator_throwsConflictException() {
        assertThrows(ConflictException.class,
                () -> requestService.create(userId, eventId));
    }

    @Test
    @DisplayName("Создание запроса: событие не опубликовано")
    void create_EventNotPublished_throwsConflictException() {
        Event event = eventRepository.findById(eventId).orElseThrow();
        event.setState(EventState.CANCELED);
        Event updatedEvent = eventRepository.save(event); // Новая переменная

        assertThrows(ConflictException.class,
                () -> requestService.create(userId, updatedEvent.getId()));
    }

    @Test
    @DisplayName("Создание запроса: достигнут лимит участников")
    void create_ParticipantLimitReached_throwsConflictException() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);
        Long secondUserId = user2.getId();

        User user3 = new User(null, "Test User 3", "user3@example.com");
        user3 = userRepository.save(user3);
        Long thirdUserId = user3.getId();

        Event event = eventRepository.findById(eventId).orElseThrow();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(1);
        eventRepository.save(event);

        Request request1 = new Request(1L, event, user2, RequestStatus.CONFIRMED, LocalDateTime.now());
        requestRepository.save(request1);


        assertThrows(ConflictException.class,
                () -> requestService.create(thirdUserId, eventId));
    }

    @Test
    @DisplayName("Создание запроса: успешно")
    void create_Success() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);
        Long secondUserId = user2.getId();

        ParticipationRequestDto dto = requestService.create(secondUserId, eventId);

        assertNotNull(dto);
        assertEquals(RequestStatus.PENDING, dto.getStatus());
    }

    @Test
    @DisplayName("Отмена запроса: пользователь не найден")
    void cancelRequest_UserNotFound_throwsNotFoundException() {
        Request request = new Request();
        request.setRequester(userRepository.findById(userId).orElseThrow());
        request.setEvent(eventRepository.findById(eventId).orElseThrow());
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        Request newRequest = requestRepository.save(request);

        assertThrows(NotFoundException.class,
                () -> requestService.cancelRequest(999L, newRequest.getId()));
    }

    @Test
    @DisplayName("Отмена запроса: запрос не найден")
    void cancelRequest_RequestNotFound_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> requestService.cancelRequest(userId, 999L));
    }

    @Test
    @DisplayName("Отмена запроса: нельзя отменить чужой запрос")
    void cancelRequest_NotOwner_throwsForbiddenException() {
        User otherUser = new User();
        otherUser.setName("Other");
        otherUser.setEmail("other@example.com");
        otherUser = userRepository.save(otherUser);

        Request request = new Request();
        request.setRequester(otherUser);
        request.setEvent(eventRepository.findById(eventId).orElseThrow());
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        Request newRequest = requestRepository.save(request);

        assertThrows(ForbiddenException.class,
                () -> requestService.cancelRequest(userId, newRequest.getId()));
    }

    @Test
    @DisplayName("Отмена запроса: успешно")
    void cancelRequest_Success() {
        Request request = new Request();
        request.setRequester(userRepository.findById(userId).orElseThrow());
        request.setEvent(eventRepository.findById(eventId).orElseThrow());
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        request = requestRepository.save(request);

        ParticipationRequestDto dto = requestService.cancelRequest(userId, request.getId());

        assertNotNull(dto);
        assertEquals(RequestStatus.CANCELED, dto.getStatus());
    }

    @Test
    @DisplayName("Получение заявок: пользователь не найден")
    void getParticipationRequests_UserNotFound_throwsNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> requestService.getParticipationRequests(999L));
    }

    @Test
    @DisplayName("Получение заявок: успешно")
    void getParticipationRequests_Success() {
        Request request = new Request();
        request.setRequester(userRepository.findById(userId).orElseThrow());
        request.setEvent(eventRepository.findById(eventId).orElseThrow());
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);

        List<ParticipationRequestDto> requests = requestService.getParticipationRequests(userId);

        assertFalse(requests.isEmpty());
    }

    @Test
    @DisplayName("Получение заявок события: пользователь не инициатор")
    void getParticipationRequestsForUserEvent_UserNotInitiator_throwsForbiddenException() {
        User user = new User();
        user.setName("Other");
        user.setEmail("other@example.com");
        User otherUser = userRepository.save(user);

        assertThrows(ForbiddenException.class,
                () -> requestService.getParticipationRequestsForUserEvent(otherUser.getId(), eventId));
    }

    @Test
    @DisplayName("Получение заявок события: успешно")
    void getParticipationRequestsForUserEvent_Success() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);
        Long secondUserId = user2.getId();

        Request request = new Request();
        request.setRequester(userRepository.findById(secondUserId).orElseThrow());
        request.setEvent(eventRepository.findById(eventId).orElseThrow());
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);

        List<ParticipationRequestDto> requests = requestService.getParticipationRequestsForUserEvent(userId, eventId);

        assertFalse(requests.isEmpty());
    }

    @Test
    @DisplayName("Обновление статуса заявок: запросов нет")
    void updateStatus_NoRequests_returnsEmptyResult() {
        RequestStatusUpdateRequest dto = new RequestStatusUpdateRequest();
        dto.setRequestIds(List.of(999L));
        dto.setStatus(RequestStatus.CONFIRMED);

        RequestStatusUpdateResult result = requestService.updateStatus(userId, eventId, dto);

        assertTrue(result.getConfirmedRequests().isEmpty());
        assertTrue(result.getRejectedRequests().isEmpty());
    }

    @Test
    @DisplayName("Обновление статуса заявок: лимит участников достигнут")
    void updateStatus_ParticipantLimitReached_throwsConflictException() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);

        User user3 = new User(null, "Test User 3", "user3@example.com");
        user3 = userRepository.save(user3);

        Event event = eventRepository.findById(eventId).orElseThrow();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(1);
        eventRepository.save(event);

        Request request1 = new Request(null, event, user2, RequestStatus.CONFIRMED, LocalDateTime.now());
        requestRepository.save(request1);
        Request request2 = new Request(null, event, user3, RequestStatus.PENDING, LocalDateTime.now());
        requestRepository.save(request2);

        RequestStatusUpdateRequest dto = new RequestStatusUpdateRequest();
        dto.setRequestIds(List.of(request2.getId()));
        dto.setStatus(RequestStatus.CONFIRMED);

        ConflictException exception = assertThrows(ConflictException.class,
                () -> requestService.updateStatus(userId, eventId, dto));

        assertTrue(exception.getMessage().contains("Достигнут лимит участников."));
    }

    @Test
    @DisplayName("Обновление статуса заявок: запрос не относится к событию")
    void updateStatus_RequestNotRelatedToEvent_throwsNotFoundException() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);

        User user3 = new User(null, "Test User 3", "user3@example.com");
        user3 = userRepository.save(user3);

        Event event = new Event();
        event.setInitiator(user3);
        event.setState(EventState.PUBLISHED);
        event.setRequestModeration(true);
        event.setParticipantLimit(2);
        event = eventRepository.save(event);
        eventId = event.getId();

        Request request1 = new Request(null, event, user2, RequestStatus.CONFIRMED, LocalDateTime.now());
        requestRepository.save(request1);

        RequestStatusUpdateRequest dto = new RequestStatusUpdateRequest();
        dto.setRequestIds(List.of(request1.getId()));
        dto.setStatus(RequestStatus.CONFIRMED);

        assertThrows(ConflictException.class,
                () -> requestService.updateStatus(userId, eventId, dto));
    }

    @Test
    @DisplayName("Обновление статуса заявок: заявка не в статусе PENDING")
    void updateStatus_RequestNotPending_throwsConflictException() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);

        Event event = eventRepository.findById(eventId).orElseThrow();

        Request request1 = new Request(null, event, user2, RequestStatus.REJECTED, LocalDateTime.now());
        requestRepository.save(request1);

        RequestStatusUpdateRequest dto = new RequestStatusUpdateRequest();
        dto.setRequestIds(List.of(request1.getId()));
        dto.setStatus(RequestStatus.CONFIRMED);

        assertThrows(ConflictException.class,
                () -> requestService.updateStatus(userId, eventId, dto));
    }

    @Test
    @DisplayName("Обновление статуса заявок: успешно")
    void updateStatus_Success() {
        User user2 = new User(null, "Test User 2", "user2@example.com");
        user2 = userRepository.save(user2);

        Event event = eventRepository.findById(eventId).orElseThrow();

        Request request1 = new Request(null, event, user2, RequestStatus.PENDING, LocalDateTime.now());
        requestRepository.save(request1);

        RequestStatusUpdateRequest dto = new RequestStatusUpdateRequest();
        dto.setRequestIds(List.of(request1.getId()));
        dto.setStatus(RequestStatus.CONFIRMED);

        RequestStatusUpdateResult result = requestService.updateStatus(userId, eventId, dto);

        assertNotNull(result);
        assertEquals(1, result.getConfirmedRequests().size());
    }
}