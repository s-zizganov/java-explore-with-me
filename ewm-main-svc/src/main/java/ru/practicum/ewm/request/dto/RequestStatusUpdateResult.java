package ru.practicum.ewm.request.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO для передачи результата массового обновления статусов заявок на участие.
 * Содержит списки подтверждённых и отклонённых заявок.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusUpdateResult {
    /**
     * Список отклонённых заявок.
     */
    List<ParticipationRequestDto> rejectedRequests;
    /**
     * Список подтверждённых заявок.
     */
    List<ParticipationRequestDto> confirmedRequests;
}
