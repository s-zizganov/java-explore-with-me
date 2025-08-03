package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;

/**
 * Полная информация о событии (DTO).
 * Используется для передачи всех данных о событии, включая метаданные, инициатора, просмотры и подтверждённые заявки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    /**
     * Идентификатор события.
     */
    Long id;
    /**
     * Заголовок события.
     */
    String title;
    /**
     * Краткое описание события (аннотация).
     */
    String annotation;
    /**
     * Полное описание события.
     */
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
    Boolean paid;
    /**
     * Лимит участников события.
     */
    Integer participantLimit;
    /**
     * Требуется ли модерация заявок на участие.
     */
    Boolean requestModeration;
    /**
     * Текущее состояние события.
     */
    EventState state;
    /**
     * Дата и время создания события.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    /**
     * Дата и время публикации события.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    /**
     * Краткая информация об инициаторе события.
     */
    UserShortDto initiator;
    /**
     * Категория события.
     */
    CategoryDto category;
    /**
     * Количество просмотров события.
     */
    long views;
    /**
     * Количество подтверждённых заявок на участие.
     */
    long confirmedRequests;
}
