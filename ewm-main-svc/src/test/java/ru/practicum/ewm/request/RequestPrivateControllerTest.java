package ru.practicum.ewm.request;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.request.controller.RequestPrivateController;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.RequestStatusUpdateResult;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestPrivateController.class)
@DisplayName("Тестирование RequestPrivateController")
public class RequestPrivateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService eventRequestService;

    private final Long userId = 1L;
    private final Long eventId = 100L;
    private final Long requestId = 500L;

    private final LocalDateTime now = LocalDateTime.now();

    @DisplayName("Создание заявки — успешно")
    @Test
    void createParticipationRequest_success_shouldReturnCreated() throws Exception {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(requestId);
        dto.setRequester(userId);
        dto.setEvent(eventId);
        dto.setStatus(RequestStatus.PENDING);
        dto.setCreated(now);

        when(eventRequestService.create(userId, eventId)).thenReturn(dto);

        mockMvc.perform(post("/users/{userId}/requests", userId)
                        .param("eventId", eventId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.requester").value(userId))
                .andExpect(jsonPath("$.event").value(eventId))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.created").value(now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @DisplayName("Отмена заявки — успешно")
    @Test
    void cancelParticipationRequest_success_shouldReturnOk() throws Exception {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(requestId);
        dto.setRequester(userId);
        dto.setEvent(eventId);
        dto.setStatus(RequestStatus.CANCELED);
        dto.setCreated(now);

        when(eventRequestService.cancelRequest(userId, requestId)).thenReturn(dto);

        mockMvc.perform(patch("/users/{userId}/requests/{requestId}/cancel", userId, requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @DisplayName("Получение всех заявок пользователя — успешно")
    @Test
    void getParticipationRequests_success_shouldReturnList() throws Exception {
        ParticipationRequestDto dto1 = new ParticipationRequestDto();
        dto1.setId(1L);
        dto1.setRequester(userId);
        dto1.setEvent(101L);
        dto1.setStatus(RequestStatus.PENDING);
        dto1.setCreated(now);

        ParticipationRequestDto dto2 = new ParticipationRequestDto();
        dto2.setId(2L);
        dto2.setRequester(userId);
        dto2.setEvent(102L);
        dto2.setStatus(RequestStatus.CONFIRMED);
        dto2.setCreated(now);

        List<ParticipationRequestDto> requests = List.of(dto1, dto2);

        when(eventRequestService.getParticipationRequests(userId)).thenReturn(requests);

        mockMvc.perform(get("/users/{userId}/requests", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].requester").value(userId))
                .andExpect(jsonPath("$[1].status").value("CONFIRMED"));
    }

    @DisplayName("Получение заявок по событию — успешно")
    @Test
    void getParticipationRequestsForUserEvent_success_shouldReturnList() throws Exception {
        ParticipationRequestDto dto1 = new ParticipationRequestDto();
        dto1.setId(3L);
        dto1.setRequester(userId);
        dto1.setEvent(eventId);
        dto1.setStatus(RequestStatus.CONFIRMED);
        dto1.setCreated(now);

        ParticipationRequestDto dto2 = new ParticipationRequestDto();
        dto2.setId(4L);
        dto2.setRequester(userId);
        dto2.setEvent(eventId);
        dto2.setStatus(RequestStatus.REJECTED);
        dto2.setCreated(now);

        List<ParticipationRequestDto> requests = List.of(dto1, dto2);

        when(eventRequestService.getParticipationRequestsForUserEvent(userId, eventId)).thenReturn(requests);

        mockMvc.perform(get("/users/{userId}/events/{eventId}/requests", userId, eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].event").value(eventId))
                .andExpect(jsonPath("$[1].status").value("REJECTED"));
    }

    @DisplayName("Обновление статуса заявок — успешно")
    @Test
    void updateStatus_success_shouldReturnOk() throws Exception {
        RequestStatusUpdateRequest request = new RequestStatusUpdateRequest();
        request.setRequestIds(List.of(6L, 7L));
        request.setStatus(RequestStatus.CONFIRMED);

        ParticipationRequestDto confirmed = new ParticipationRequestDto();
        confirmed.setId(6L);
        confirmed.setRequester(userId);
        confirmed.setEvent(eventId);
        confirmed.setStatus(RequestStatus.CONFIRMED);
        confirmed.setCreated(now);

        ParticipationRequestDto rejected = new ParticipationRequestDto();
        rejected.setId(7L);
        rejected.setRequester(userId);
        rejected.setEvent(eventId);
        rejected.setStatus(RequestStatus.REJECTED);
        rejected.setCreated(now);

        RequestStatusUpdateResult result = new RequestStatusUpdateResult();
        result.setConfirmedRequests(List.of(confirmed));
        result.setRejectedRequests(List.of(rejected));

        when(eventRequestService.updateStatus(userId, eventId, request)).thenReturn(result);

        mockMvc.perform(patch("/users/{userId}/events/{eventId}/requests", userId, eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmedRequests.length()").value(1))
                .andExpect(jsonPath("$.rejectedRequests.length()").value(1));
    }
}