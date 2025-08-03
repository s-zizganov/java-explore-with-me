package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.utils.RequestStatus;

import java.time.LocalDateTime;

/**
 * DTO для передачи информации о заявке на участие в событии.
 * Содержит данные о времени создания, событии, пользователе и статусе заявки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    /**
     * Дата и время создания заявки.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    /**
     * Идентификатор события, на участие в котором подана заявка.
     */
    Long event;
    /**
     * Идентификатор заявки.
     */
    Long id;
    /**
     * Идентификатор пользователя, подавшего заявку.
     */
    Long requester;
    /**
     * Статус заявки (например, CONFIRMED, PENDING, REJECTED).
     */
    RequestStatus status;
}