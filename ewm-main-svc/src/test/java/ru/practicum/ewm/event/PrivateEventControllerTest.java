package ru.practicum.ewm.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.event.controller.PrivateEventController;
import ru.practicum.ewm.event.dto.EventCreateDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PrivateEventController.class)
@DisplayName("Тестирование PrivateEventController")
public class PrivateEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @Test
    void createEvent_correctRequest_shouldReturnCreatedEvent() throws Exception {
        // Arrange
        Long userId = 1L;
        EventCreateDto createDto = new EventCreateDto();
        createDto.setTitle("New Event");
        createDto.setDescription("Too long event description");
        createDto.setAnnotation("This is an annotation");

        EventFullDto createdEvent = new EventFullDto();
        createdEvent.setId(1L);
        createdEvent.setTitle("New Event");
        createdEvent.setDescription("Too long event description");
        createdEvent.setAnnotation("This is an annotation"); // Добавьте аннотацию

        when(eventService.create(eq(userId), any(EventCreateDto.class))).thenReturn(createdEvent);

        mockMvc.perform(post("/users/{userId}/events", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Event"))
                .andExpect(jsonPath("$.description").value("Too long event description"))
                .andExpect(jsonPath("$.annotation").value("This is an annotation"));

        verify(eventService, times(1)).create(eq(userId), any(EventCreateDto.class));
    }

    @DisplayName("Успешное обновление события")
    @Test
    void updateEvent_correctRequest_shouldReturnUpdatedEvent() throws Exception {
        Long userId = 1L;
        Long eventId = 1L;
        UpdateEventRequest updateRequest = new UpdateEventRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("This is an updated description with more than 20 characters");

        EventFullDto updatedEvent = new EventFullDto();
        updatedEvent.setId(eventId);
        updatedEvent.setTitle("Updated Title");
        updatedEvent.setDescription("This is an updated description with more than 20 characters");

        when(eventService.updateEventByPrivate(eq(userId), eq(eventId), any(UpdateEventRequest.class)))
                .thenReturn(updatedEvent);

        mockMvc.perform(patch("/users/{userId}/events/{eventId}", userId, eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("This is an updated description with more than 20 characters"));

        verify(eventService, times(1)).updateEventByPrivate(eq(userId), eq(eventId), any(UpdateEventRequest.class));
    }

    @DisplayName("Успешное получение события пользователя")
    @Test
    void getEventOfUser_correctParams_shouldReturnEvent() throws Exception {
        Long userId = 1L;
        Long eventId = 1L;

        EventFullDto event = new EventFullDto();
        event.setId(eventId);
        event.setTitle("Event Title");
        event.setDescription("Event Description");

        when(eventService.getEventOfUser(eq(userId), eq(eventId))).thenReturn(event);

        mockMvc.perform(get("/users/{userId}/events/{eventId}", userId, eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.title").value("Event Title"))
                .andExpect(jsonPath("$.description").value("Event Description"));

        verify(eventService, times(1)).getEventOfUser(eq(userId), eq(eventId));
    }

    @DisplayName("Успешное получение событий пользователя")
    @Test
    void findAllByPrivate_correctParams_shouldReturnEvents() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        EventShortDto event1 = new EventShortDto();
        event1.setId(1L);
        event1.setTitle("Event 1");

        EventShortDto event2 = new EventShortDto();
        event2.setId(2L);
        event2.setTitle("Event 2");

        Collection<EventShortDto> events = List.of(event1, event2);

        when(eventService.findAllByPrivate(eq(userId), eq(from), eq(size), any(HttpServletRequest.class)))
                .thenReturn(events);

        mockMvc.perform(get("/users/{userId}/events", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Event 2"));

        verify(eventService, times(1)).findAllByPrivate(eq(userId), eq(from), eq(size),
                any(HttpServletRequest.class));
    }
}