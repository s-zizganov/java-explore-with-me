package ru.practicum.ewm.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование RequestMapper")
public class RequestMapperTest {
    @Autowired
    RequestMapper requestMapper;

    @DisplayName("Преобразовать корректный Request в ParticipationRequestDto")
    @Test
    void toRequestDto_allRequestFieldsFilled_returnCorrectParticipationRequestDto() {
        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(2L);
        Request request = new Request(1L, event, user, RequestStatus.REJECTED, LocalDateTime.now());
        ParticipationRequestDto requestDto = requestMapper.toRequestDto(request);
        assertEquals(requestDto.getId(), request.getId());
        assertEquals(requestDto.getRequester(), request.getRequester().getId());
        assertEquals(requestDto.getStatus(), request.getStatus());
        assertEquals(requestDto.getEvent(), request.getEvent().getId());
        assertEquals(requestDto.getCreated(), request.getCreated());
    }

    @DisplayName("Преобразовать null в ParticipationRequestDto")
    @Test
    void toRequestDto_withNullRequest_returnNullParticipationRequestDto() {
        ParticipationRequestDto requestDto = requestMapper.toRequestDto(null);
        assertNull(requestDto);
    }
}
