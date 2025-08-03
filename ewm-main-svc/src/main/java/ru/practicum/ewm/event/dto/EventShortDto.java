package ru.practicum.ewm.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

/**
 * Краткая информация о событии (DTO).
 * Используется для отображения событий в списках и кратких представлениях.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    /**
     * Идентификатор события.
     */
    Long id;
    /**
     * Краткое описание события (аннотация).
     */
    String annotation;
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
    /**
     * Дата и время проведения события.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    /**
     * Признак платности события.
     */
    Boolean paid;
    /**
     * Краткая информация об инициаторе события.
     */
    UserShortDto initiator;
    /**
     * Заголовок события.
     */
    String title;

}