package ru.practicum.ewm.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventCreateDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.utils.EventState;
import ru.practicum.ewm.utils.StateAction;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование EventServiceImp")
public class EventServiceImpCreateAndUpdateTest {
    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventRepository eventRepository;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("testuser@example.com");
        userRepository.save(testUser);

        testCategory = new Category();
        testCategory.setName("Test Category");
        categoryRepository.save(testCategory);
    }

    private Event createTestEvent() {
        Event event = new Event();
        event.setTitle("Test Event");
        event.setAnnotation("Test Annotation");
        event.setDescription("Test Description");
        event.setEventDate(LocalDateTime.now().plusHours(3));
        event.setPaid(false);
        event.setParticipantLimit(100);
        event.setRequestModeration(true);
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(testUser);
        event.setCategory(testCategory);

        return eventRepository.save(event);
    }

    @DisplayName("Создание нового события")
    @Test
    void create_ValidEvent_ReturnsEventFullDto() {
        EventCreateDto eventCreateDto = new EventCreateDto();
        eventCreateDto.setAnnotation("Test annotation");
        eventCreateDto.setDescription("Test description");
        eventCreateDto.setEventDate(LocalDateTime.now().plusHours(3));
        eventCreateDto.setPaid(true);
        eventCreateDto.setParticipantLimit(100);
        eventCreateDto.setCategory(testCategory.getId());

        LocationDto locationDto = new LocationDto();
        locationDto.setLat(55.7558f);
        locationDto.setLon(37.6173f);
        eventCreateDto.setLocation(locationDto);

        EventFullDto result = eventService.create(testUser.getId(), eventCreateDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(eventCreateDto.getAnnotation(), result.getAnnotation());
        assertEquals(eventCreateDto.getDescription(), result.getDescription());
        assertEquals(eventCreateDto.getPaid(), result.getPaid());
        assertEquals(eventCreateDto.getParticipantLimit(), result.getParticipantLimit());
        assertEquals(EventState.PENDING, result.getState());
    }

    @DisplayName("Проверка, что нельзя создать событие с несуществующей категорией")
    @Test
    void create_NonExistentCategory_ThrowsNotFoundException() {
        EventCreateDto eventCreateDto = new EventCreateDto();
        eventCreateDto.setAnnotation("Test annotation");
        eventCreateDto.setDescription("Test description");
        eventCreateDto.setEventDate(LocalDateTime.now().plusHours(3));
        eventCreateDto.setPaid(true);
        eventCreateDto.setParticipantLimit(100);

        Long nonExistentCategoryId = 999L;
        eventCreateDto.setCategory(nonExistentCategoryId);

        LocationDto locationDto = new LocationDto();
        locationDto.setLat(55.7558f);
        locationDto.setLon(37.6173f);
        eventCreateDto.setLocation(locationDto);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.create(testUser.getId(), eventCreateDto),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );

        assertTrue(exception.getMessage().contains("Категория с ID " + nonExistentCategoryId + " не найдена"));
    }

    @DisplayName("Проверка, что выбрасывается NotFoundException при обновлении несуществующего события")
    @Test
    void updateEventByAdmin_NonExistentEvent_ThrowsNotFoundException() {
        Long nonExistentEventId = 999L;
        UpdateEventRequest adminRequest = new UpdateEventRequest();

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.updateEventByAdmin(nonExistentEventId, adminRequest),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );
    }

    @DisplayName("Проверка, что выбрасывается NotFoundException при указании несуществующей категории")
    @Test
    void updateEventByAdmin_NonExistentCategory_ThrowsNotFoundException() {
        Event event = createTestEvent();
        UpdateEventRequest adminRequest = new UpdateEventRequest();
        adminRequest.setCategory(999L); // Категория не существует

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.updateEventByAdmin(event.getId(), adminRequest),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );

        assertTrue(exception.getMessage().contains("Категория с ID 999 не найдена"));
    }

    @DisplayName("Проверка успешного обновления события администратором")
    @Test
    void updateEventByAdmin_ValidRequest_ReturnsUpdatedEvent() {
        Event event = createTestEvent();
        UpdateEventRequest adminRequest = new UpdateEventRequest();
        adminRequest.setTitle("Updated Title");
        adminRequest.setStateAction(StateAction.PUBLISH_EVENT);

        EventFullDto result = eventService.updateEventByAdmin(event.getId(), adminRequest);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals(EventState.PUBLISHED, result.getState());
    }

    @DisplayName("Проверка, что выбрасывается NotFoundException при попытке обновления события несуществующим пользователем")
    @Test
    void updateEventByPrivate_NonExistentUser_ThrowsNotFoundException() {
        Long nonExistentUserId = 999L;
        Event event = createTestEvent();
        UpdateEventRequest eventUserRequest = new UpdateEventRequest();

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.updateEventByPrivate(nonExistentUserId, event.getId(), eventUserRequest),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );
    }

    @DisplayName("Проверка, что выбрасывается NotFoundException при попытке обновления несуществующего события")
    @Test
    void updateEventByPrivate_NonExistentEvent_ThrowsNotFoundException() {
        Long nonExistentEventId = 999L;
        UpdateEventRequest eventUserRequest = new UpdateEventRequest();

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.updateEventByPrivate(testUser.getId(), nonExistentEventId, eventUserRequest),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );
    }

    @DisplayName("Проверка, что выбрасывается NotFoundException при попытке изменения события другим пользователем")
    @Test
    void updateEventByPrivate_WrongUser_ThrowsNotFoundException() {
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("anotheruser@example.com");
        userRepository.save(anotherUser);

        Event event = createTestEvent();
        UpdateEventRequest eventUserRequest = new UpdateEventRequest();

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.updateEventByPrivate(anotherUser.getId(), event.getId(), eventUserRequest),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );
    }

    @DisplayName("Проверка, что выбрасывается ConflictException при попытке изменения опубликованного события")
    @Test
    void updateEventByPrivate_PublishedEvent_ThrowsConflictException() {
        Event event = createTestEvent();
        event.setState(EventState.PUBLISHED);
        eventRepository.save(event);

        UpdateEventRequest eventUserRequest = new UpdateEventRequest();

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> eventService.updateEventByPrivate(testUser.getId(), event.getId(), eventUserRequest),
                "Ожидается, что будет выброшено исключение ConflictException"
        );

    }

    @DisplayName("Проверка, что выбрасывается NotFoundException при указании несуществующей категории")
    @Test
    void updateEventByPrivate_NonExistentCategory_ThrowsNotFoundException() {
        Event event = createTestEvent();

        UpdateEventRequest eventUserRequest = new UpdateEventRequest();
        eventUserRequest.setCategory(999L); // Категория не существует

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> eventService.updateEventByPrivate(testUser.getId(), event.getId(), eventUserRequest),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );

    }

    @DisplayName("Проверка успешного обновления события")
    @Test
    void updateEventByPrivate_ValidRequest_ReturnsUpdatedEvent() {
        Event event = createTestEvent();

        UpdateEventRequest eventUserRequest = new UpdateEventRequest();
        eventUserRequest.setTitle("Updated Title");

        EventFullDto result = eventService.updateEventByPrivate(testUser.getId(), event.getId(), eventUserRequest);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }
}