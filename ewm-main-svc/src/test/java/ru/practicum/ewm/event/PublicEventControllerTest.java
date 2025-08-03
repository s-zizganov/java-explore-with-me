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
import ru.practicum.ewm.event.controller.PublicEventController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventSearchDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicEventController.class)
@DisplayName("Тестирование PublicEventController")
public class PublicEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @DisplayName("Успешное получение событий с фильтром")
    @Test
    void findAllByPublic_correctParams_shouldReturnEvents() throws Exception {
        EventSearchDto searchParams = new EventSearchDto();
        searchParams.setText("concert");
        searchParams.setPaid(true);

        EventShortDto event1 = new EventShortDto();
        event1.setId(1L);
        event1.setTitle("Concert 1");

        EventShortDto event2 = new EventShortDto();
        event2.setId(2L);
        event2.setTitle("Concert 2");

        Collection<EventShortDto> events = List.of(event1, event2);

        when(eventService.findAllByPublic(any(EventSearchDto.class), any(HttpServletRequest.class)))
                .thenReturn(events);

        mockMvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "concert")
                        .param("paid", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Concert 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Concert 2"));

        verify(eventService, times(1)).findAllByPublic(any(EventSearchDto.class),
                any(HttpServletRequest.class));
    }

    @DisplayName("Успешное получение события по ID")
    @Test
    void findEventById_correctId_shouldReturnEvent() throws Exception {
        Long eventId = 1L;

        EventFullDto event = new EventFullDto();
        event.setId(eventId);
        event.setTitle("Event Title");
        event.setDescription("Event Description");

        when(eventService.findEventById(eq(eventId), any(HttpServletRequest.class))).thenReturn(event);

        mockMvc.perform(get("/events/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.title").value("Event Title"))
                .andExpect(jsonPath("$.description").value("Event Description"));

        verify(eventService, times(1)).findEventById(eq(eventId), any(HttpServletRequest.class));
    }
}