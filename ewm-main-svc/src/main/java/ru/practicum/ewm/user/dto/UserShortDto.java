package ru.practicum.ewm.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Краткое представление пользователя (DTO).
 * Используется для передачи минимальной информации о пользователе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {
    /**
     * Идентификатор пользователя.
     */
    Long id;
    /**
     * Имя пользователя.
     */
    String name;
}
