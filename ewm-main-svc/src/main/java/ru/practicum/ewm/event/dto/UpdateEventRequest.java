package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.utils.StateAction;

import java.time.LocalDateTime;

/**
 * DTO для обновления данных события.
 * Используется для передачи изменённых данных при редактировании события.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventRequest {
    /**
     * Краткое описание события (аннотация).
     */
    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть от 20 до 2000 символов")
    String annotation;
    /**
     * Идентификатор категории события.
     */
    Long category;
    /**
     * Полное описание события.
     */
    @Size(min = 20, max = 7000, message = "Длина описания должна быть от 20 до 7000 символов")
    String description;
    /**
     * Заголовок события.
     */
    @Size(min = 3, max = 120, message = "Длина аннотации должна быть от 3 до 120 символов")
    String title;
    /**
     * Дата и время проведения события.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    /**
     * Местоположение события.
     */
    Location location;
    /**
     * Признак платности события.
     */
    Boolean paid;
    /**
     * Лимит участников события.
     */
    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    Integer participantLimit;
    /**
     * Требуется ли модерация заявок на участие.
     */
    Boolean requestModeration;
    /**
     * Действие со статусом события (например, отправить на модерацию, опубликовать и т.д.).
     */
    StateAction stateAction;

}