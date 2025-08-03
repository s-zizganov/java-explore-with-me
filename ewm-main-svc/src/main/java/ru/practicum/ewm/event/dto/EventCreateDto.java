package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.location.dto.LocationDto;

import java.time.LocalDateTime;

/**
 * DTO для создания нового события.
 * Используется для передачи данных при создании события.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreateDto {
    /**
     * Краткое описание события (аннотация).
     */
    @NotBlank
    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть от 20 до 2000 символов")
    String annotation;
    /**
     * Идентификатор категории события.
     */
    Long category;
    /**
     * Полное описание события.
     */
    @NotBlank
    @Size(min = 20, max = 7000, message = "Длина описания должна быть от 20 до 7000 символов")
    String description;
    /**
     * Дата и время проведения события.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    /**
     * Местоположение события.
     */
    LocationDto location;
    /**
     * Признак платности события.
     */
    Boolean paid = false;
    /**
     * Лимит участников события.
     */
    @Min(value = 0, message = "Лимит участников не может быть отрицательным")
    Integer participantLimit = 0;
    /**
     * Требуется ли модерация заявок на участие.
     */
    Boolean requestModeration = true;
    /**
     * Заголовок события.
     */
    @Size(min = 3, max = 120, message = "Длина аннотации должна быть от 3 до 120 символов")
    String title;
}