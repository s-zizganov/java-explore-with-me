package ru.practicum.ewm.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.utils.RequestStatus;

import java.util.List;

/**
 * DTO для обновления статусов заявок на участие в событии.
 * Используется для передачи списка идентификаторов заявок и нового статуса.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusUpdateRequest {
    /**
     * Список идентификаторов заявок, для которых требуется обновить статус.
     */
    @NotEmpty(message = "Ваш список запросов пуст")
    List<Long> requestIds;

    /**
     * Новый статус для заявок (например, CONFIRMED, REJECTED).
     */
    @NotBlank(message = "Статус запросов не указан")
    RequestStatus status;
}
