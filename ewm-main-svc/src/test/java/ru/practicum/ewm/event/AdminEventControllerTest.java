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
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.event.controller.AdminEventController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventSearchDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.utils.EventState;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminEventController.class)
@DisplayName("Тестирование AdminEventController")
public class AdminEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @DisplayName("Успешное получение событий с фильтром")
    @Test
    void findAllByAdmin_correctParams_shouldReturnEvents() throws Exception {
        EventSearchDto searchParams = new EventSearchDto();
        searchParams.setText("concert");
        searchParams.setCategories(List.of(1L, 2L));
        searchParams.setPaid(true);
        searchParams.setRangeStart(LocalDateTime.of(2023, 1, 1, 10, 0, 0));
        searchParams.setRangeEnd(LocalDateTime.of(2023, 12, 31, 23, 59, 59));
        searchParams.setOnlyAvailable(true);
        searchParams.setSort("EVENT_DATE");
        searchParams.setFrom(0);
        searchParams.setSize(10);
        searchParams.setUsers(List.of(1L, 2L));
        searchParams.setStates(List.of(EventState.PUBLISHED));

        EventFullDto event1 = createEventFullDto(1L, "Concert 1", "Description 1");
        EventFullDto event2 = createEventFullDto(2L, "Concert 2", "Description 2");
        Collection<EventFullDto> events = List.of(event1, event2);

        when(eventService.findAllByAdmin(any(EventSearchDto.class), any(HttpServletRequest.class)))
                .thenReturn(events);

       mockMvc.perform(get("/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "concert")
                        .param("categories", "1", "2")
                        .param("paid", "true")
                        .param("rangeStart", "2023-01-01 10:00:00")
                        .param("rangeEnd", "2023-12-31 23:59:59")
                        .param("onlyAvailable", "true")
                        .param("sort", "EVENT_DATE")
                        .param("from", "0")
                        .param("size", "10")
                        .param("users", "1", "2")
                        .param("states", "PUBLISHED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Concert 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Concert 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"));

        verify(eventService, times(1)).findAllByAdmin(any(EventSearchDto.class), any(HttpServletRequest.class));
    }

    @DisplayName("Успешное обновление события")
    @Test
    void updateEvent_correctRequest_shouldReturnUpdatedEvent() throws Exception {
        Long eventId = 1L;
        UpdateEventRequest updateRequest = new UpdateEventRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description Updated Description");

        EventFullDto updatedEvent = createEventFullDto(1L, "Updated Title", "Updated Description Updated Description");

        when(eventService.updateEventByAdmin(eq(eventId), any(UpdateEventRequest.class)))
                .thenReturn(updatedEvent);

        mockMvc.perform(patch("/admin/events/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description Updated Description"))
                .andExpect(jsonPath("$.eventDate").exists())
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.paid").isBoolean())
                .andExpect(jsonPath("$.participantLimit").isNumber())
                .andExpect(jsonPath("$.requestModeration").isBoolean())
                .andExpect(jsonPath("$.state").exists())
                .andExpect(jsonPath("$.createdOn").exists())
                .andExpect(jsonPath("$.publishedOn").exists())
                .andExpect(jsonPath("$.initiator").exists())
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.views").isNumber())
                .andExpect(jsonPath("$.confirmedRequests").isNumber());

        verify(eventService, times(1)).updateEventByAdmin(eq(eventId), any(UpdateEventRequest.class));
    }

    private EventFullDto createEventFullDto(Long id, String title, String description) {
        return EventFullDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .annotation("Annotation for " + title + " some text to 20 literals")
                .eventDate(LocalDateTime.now())
                .location(new LocationDto(55.7522f, 37.6156f))
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .state(EventState.PUBLISHED)
                .createdOn(LocalDateTime.now().minusDays(1))
                .publishedOn(LocalDateTime.now())
                .initiator(new UserShortDto(1L, "User Name"))
                .category(new CategoryDto("Category Name"))
                .views(1000L)
                .confirmedRequests(500L)
                .build();
    }
}