package ru.practicum.ewm.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.event.dto.EventCreateDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Тестирование EventMapper")
class EventMapperTest {

    @Autowired
    EventMapper eventMapper;

    @DisplayName("Преобразовать корректный EventCreateDto в Event")
    @Test
    void toEvent_allFieldsFilled_returnCorrectEvent() {
        LocationDto locationDto = new LocationDto(55.7558f,37.6176f);

        EventCreateDto dto = new EventCreateDto();
        dto.setAnnotation("Short annotation");
        dto.setCategory(1L);
        dto.setDescription("Full description");
        dto.setEventDate(LocalDateTime.of(2025, 12, 31, 15, 0));
        dto.setLocation(locationDto);
        dto.setPaid(true);
        dto.setParticipantLimit(100);
        dto.setRequestModeration(true);
        dto.setTitle("Festival");

        Event event = eventMapper.toEvent(dto, EventState.PENDING);

        assertEquals(dto.getAnnotation(), event.getAnnotation());
        assertEquals(dto.getDescription(), event.getDescription());
        assertEquals(dto.getEventDate(), event.getEventDate());
        assertEquals(dto.getPaid(), event.getPaid());
        assertEquals(dto.getParticipantLimit(), event.getParticipantLimit());
        assertEquals(dto.getRequestModeration(), event.getRequestModeration());
        assertEquals(dto.getTitle(), event.getTitle());
    }

    @DisplayName("Преобразовать Event в EventShortDto")
    @Test
    void toEvent_toShortDto_returnCorrectEventShortDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Workshop");

        User initiator = new User();
        initiator.setId(1L);
        initiator.setName("Ivan");

        Location location = new Location(2L, 55.7558f,37.6176f);

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Festival");
        event.setAnnotation("Short annotation");
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setLocation(location);
        event.setEventDate(LocalDateTime.of(2025, 12, 31, 15, 0));
        event.setPaid(true);
        event.setConfirmedRequests(10);
        event.setViews(150L);

        EventShortDto shortDto = eventMapper.toShortDto(event);

        assertEquals(event.getId(), shortDto.getId());
        assertEquals(event.getTitle(), shortDto.getTitle());
        assertEquals(event.getAnnotation(), shortDto.getAnnotation());

        assertNotNull(shortDto.getCategory());
        assertEquals(category.getName(), shortDto.getCategory().getName());

        assertNotNull(shortDto.getInitiator());
        assertEquals(initiator.getName(), shortDto.getInitiator().getName());

        assertEquals(event.getEventDate(), shortDto.getEventDate());
        assertEquals(event.getPaid(), shortDto.getPaid());
        assertEquals(event.getConfirmedRequests(), shortDto.getConfirmedRequests());
        assertEquals(event.getViews(), shortDto.getViews());
    }

    @DisplayName("Преобразовать null в Event — вернуть null")
    @Test
    void toEvent_withNullEventCreateDto_returnNull() {
        Event result = eventMapper.toEvent(null, null);
        assertNull(result);
    }

    @DisplayName("Преобразовать null в EventShortDto — вернуть null")
    @Test
    void toShortDto_withNullEvent_returnNull() {
        EventShortDto result = eventMapper.toShortDto(null);
        assertNull(result);
    }
}