package ru.practicum.ewm.event;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;
import ru.practicum.ewm.event.dto.EventSearchDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.IncorrectRequestException;
import ru.practicum.ewm.user.dto.UserCreateDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование метода findAllByPublic")
public class EventServiceFindAllByPublicTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EventRepository eventRepository;

    @DisplayName("Успешный поиск событий")
    @Test
    void findAllByPublic_Success() {
        User user = userService.createUser(new UserCreateDto("User", "user@email.ru"));
        Category category = categoryService.create(new CategoryDto("Category"));
        Event event = new Event();
        event.setTitle("Event");
        event.setAnnotation("Annotation");
        event.setDescription("Description");
        event.setCategory(category);
        event.setInitiator(user);
        event.setState(EventState.PUBLISHED);
        event.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event);

        EventSearchDto params = new EventSearchDto();
        params.setFrom(0);
        params.setSize(10);
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        Collection<EventShortDto> events = eventService.findAllByPublic(params, mockRequest);

        assertNotNull(events);
        assertEquals(1, events.size());
    }

    @DisplayName("Проверка фильтрации по тексту")
    @Test
    void findAllByPublic_FilterByText() {
        User user = userService.createUser(new UserCreateDto("User", "user@email.ru"));
        Category category = categoryService.create(new CategoryDto("Category"));
        Event event1 = new Event();
        event1.setTitle("Event");
        event1.setAnnotation("Test Annotation");
        event1.setDescription("Description");
        event1.setCategory(category);
        event1.setInitiator(user);
        event1.setState(EventState.PUBLISHED);
        event1.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Another Event");
        event2.setAnnotation("Annotation");
        event2.setDescription("Description");
        event2.setCategory(category);
        event2.setInitiator(user);
        event2.setState(EventState.PUBLISHED);
        event2.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event2);

        EventSearchDto params = new EventSearchDto();
        params.setText("test");
        params.setFrom(0);
        params.setSize(10);
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        Collection<EventShortDto> events = eventService.findAllByPublic(params, mockRequest);

        assertNotNull(events);
        assertEquals(1, events.size());
    }

    @DisplayName("Проверка фильтрации по категории")
    @Test
    void findAllByPublic_FilterByCategory() {
        User user = userService.createUser(new UserCreateDto("User", "user@email.ru"));
        Category category1 = categoryService.create(new CategoryDto("Category 1"));
        Category category2 = categoryService.create(new CategoryDto("Category 2"));
        Event event1 = new Event();
        event1.setTitle("Event 1");
        event1.setCategory(category1);
        event1.setInitiator(user);
        event1.setState(EventState.PUBLISHED);
        event1.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Event 2");
        event2.setCategory(category2);
        event2.setInitiator(user);
        event2.setState(EventState.PUBLISHED);
        event2.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event2);

        EventSearchDto params = new EventSearchDto();
        params.setCategories(List.of(category1.getId()));
        params.setFrom(0);
        params.setSize(10);
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        Collection<EventShortDto> events = eventService.findAllByPublic(params, mockRequest);

        assertNotNull(events);
        assertEquals(1, events.size());
    }

    @DisplayName("Проверка фильтрации по платности")
    @Test
    void findAllByPublic_FilterByPaid() {
        User user = userService.createUser(new UserCreateDto("User", "user@email.ru"));
        Category category = categoryService.create(new CategoryDto("Category"));
        Event event1 = new Event();
        event1.setTitle("Free Event");
        event1.setCategory(category);
        event1.setInitiator(user);
        event1.setState(EventState.PUBLISHED);
        event1.setPaid(false);
        event1.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Paid Event");
        event2.setCategory(category);
        event2.setInitiator(user);
        event2.setState(EventState.PUBLISHED);
        event2.setPaid(true);
        event2.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event2);

        EventSearchDto params = new EventSearchDto();
        params.setPaid(false);
        params.setFrom(0);
        params.setSize(10);
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        Collection<EventShortDto> events = eventService.findAllByPublic(params, mockRequest);

        assertNotNull(events);
        assertEquals(1, events.size());
    }

    @DisplayName("Проверка фильтрации по дате")
    @Test
    void findAllByPublic_FilterByDate() {
        User user = userService.createUser(new UserCreateDto("User", "user@email.ru"));
        Category category = categoryService.create(new CategoryDto("Category"));
        Event event1 = new Event();
        event1.setTitle("Past Event");
        event1.setCategory(category);
        event1.setInitiator(user);
        event1.setState(EventState.PUBLISHED);
        event1.setEventDate(LocalDateTime.now().minusDays(1));
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Future Event");
        event2.setCategory(category);
        event2.setInitiator(user);
        event2.setState(EventState.PUBLISHED);
        event2.setEventDate(LocalDateTime.now().plusDays(1));
        eventRepository.save(event2);

        EventSearchDto params = new EventSearchDto();
        params.setRangeStart(LocalDateTime.now());
        params.setRangeEnd(LocalDateTime.now().plusDays(2));
        params.setFrom(0);
        params.setSize(10);
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        Collection<EventShortDto> events = eventService.findAllByPublic(params, mockRequest);

        assertNotNull(events);
        assertEquals(1, events.size());
    }

    @DisplayName("Проверка исключения для некорректного диапазона дат")
    @Test
    void findAllByPublic_InvalidDateRange_ThrowsException() {
        EventSearchDto params = new EventSearchDto();
        params.setRangeStart(LocalDateTime.now().plusDays(1));
        params.setRangeEnd(LocalDateTime.now());
        params.setFrom(0);
        params.setSize(10);
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.findAllByPublic(params, mockRequest),
                "Ожидается исключение IllegalArgumentException"
        );

        assertTrue(exception.getMessage().contains("rangeStart должен быть раньше rangeEnd"));
    }

    @DisplayName("Проверка исключения для некорректного типа сортировки")
    @Test
    void findAllByPublic_InvalidSortType_ThrowsException() {
        EventSearchDto params = new EventSearchDto();
        params.setSort("INVALID_SORT");
        params.setFrom(0);
        params.setSize(10);
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        IncorrectRequestException exception = assertThrows(
                IncorrectRequestException.class,
                () -> eventService.findAllByPublic(params, mockRequest),
                "Ожидается исключение IncorrectRequestException"
        );

        assertTrue(exception.getMessage().contains("Unknown sort type"));
    }
}