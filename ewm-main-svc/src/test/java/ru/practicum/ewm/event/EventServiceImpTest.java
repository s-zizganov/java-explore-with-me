package ru.practicum.ewm.event;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventSearchDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.utils.EventState;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование EventService")
public class EventServiceImpTest {
    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @DisplayName("Получение события пользователя (успешно)")
    @Test
    void getEventOfUser_Success() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");
        User user = userService.createUser(userCreateDto);

        Event event = new Event();
        event.setTitle("Test Event");
        event.setInitiator(user);
        eventRepository.save(event);

        EventFullDto eventFullDto = eventService.getEventOfUser(user.getId(), event.getId());

        assertNotNull(eventFullDto);
        assertEquals(event.getTitle(), eventFullDto.getTitle());
        assertEquals(user.getId(), eventFullDto.getInitiator().getId());
    }

    @DisplayName("Получение события пользователя (пользователь не является инициатором)")
    @Test
    void getEventOfUser_UserNotInitiator_ThrowsValidationException() {
        UserCreateDto user1Dto = new UserCreateDto("User One", "user1@email.ru");
        User user1 = userService.createUser(user1Dto);

        UserCreateDto user2Dto = new UserCreateDto("User Two", "user2@email.ru");
        User user2 = userService.createUser(user2Dto);

        Event event = new Event();
        event.setTitle("Test Event");
        event.setInitiator(user1);
        eventRepository.save(event);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> eventService.getEventOfUser(user2.getId(), event.getId()),
                "Ожидается исключение ValidationException"
        );
    }

    @DisplayName("Получение списка событий пользователя (успешно)")
    @Test
    void findAllByPrivate_Success() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");
        User user = userService.createUser(userCreateDto);

        Event event1 = new Event();
        event1.setTitle("Event 1");
        event1.setInitiator(user);
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Event 2");
        event2.setInitiator(user);
        eventRepository.save(event2);

        HttpServletRequest mockRequest = new MockHttpServletRequest();

        Collection<EventShortDto> events = eventService.findAllByPrivate(user.getId(), 0, 10, mockRequest);

        // Проверяем результат
        assertNotNull(events);
        assertEquals(2, events.size());
    }

    @DisplayName("Получение списка событий пользователя (пустой список)")
    @Test
    void findAllByPrivate_EmptyList() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");
        User user = userService.createUser(userCreateDto);

        Collection<EventShortDto> events = eventService.findAllByPrivate(user.getId(), 0, 10, null);

        assertNotNull(events);
        assertEquals(0, events.size());
    }

    @DisplayName("Получение списка событий администратором (успешно)")
    @Test
    void findAllByAdmin_Success() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");
        User user = userService.createUser(userCreateDto);

        Event event1 = new Event();
        event1.setTitle("Event 1");
        event1.setInitiator(user);
        event1.setState(EventState.PUBLISHED);
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Event 2");
        event2.setInitiator(user);
        event2.setState(EventState.CANCELED);
        eventRepository.save(event2);

        EventSearchDto params = new EventSearchDto();
        params.setUsers(List.of(user.getId()));
        params.setStates(List.of(EventState.PUBLISHED));
        params.setFrom(0);
        params.setSize(10);

        HttpServletRequest mockRequest = new MockHttpServletRequest();
        Collection<EventFullDto> events = eventService.findAllByAdmin(params, mockRequest);

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Event 1", events.iterator().next().getTitle());
    }

    @DisplayName("Получение списка событий администратором (пустой список)")
    @Test
    void findAllByAdmin_EmptyList() {
        EventSearchDto params = new EventSearchDto();
        params.setUsers(List.of(999L));
        params.setFrom(0);
        params.setSize(10);

        Collection<EventFullDto> events = eventService.findAllByAdmin(params, null);

        assertNotNull(events);
        assertEquals(0, events.size());
    }

    @DisplayName("Получение опубликованного события по ID (успешно)")
    @Test
    void findEventById_PublishedEvent_Success() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");
        User user = userService.createUser(userCreateDto);

        Event event = new Event();
        event.setTitle("Test Event");
        event.setInitiator(user);
        event.setState(EventState.PUBLISHED);
        eventRepository.save(event);

        HttpServletRequest mockRequest = new MockHttpServletRequest();
        EventFullDto eventFullDto = eventService.findEventById(event.getId(), mockRequest);

        assertNotNull(eventFullDto);
        assertEquals(event.getTitle(), eventFullDto.getTitle());
    }

    @DisplayName("Получение неопубликованного события по ID (выбрасывается исключение)")
    @Test
    void findEventById_UnpublishedEvent_ThrowsNotFoundException() {
        UserCreateDto userCreateDto = new UserCreateDto("User name", "user@email.ru");
        User user = userService.createUser(userCreateDto);

        Event event = new Event();
        event.setTitle("Test Event");
        event.setInitiator(user);
        event.setState(EventState.CANCELED);
        eventRepository.save(event);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.findEventById(event.getId(), null),
                "Ожидается исключение NotFoundException"
        );

        assertTrue(exception.getMessage().contains("Событие с ID = " + event.getId() + " не опубликовано"));
    }
}
