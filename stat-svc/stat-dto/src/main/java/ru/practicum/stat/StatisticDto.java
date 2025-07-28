package ru.practicum.stat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO для передачи агрегированной статистики по посещениям.
 * Используется для отображения количества хитов (просмотров) по каждому приложению и URI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticDto {
    /**
     * Имя приложения, для которого собирается статистика.
     */
    String app;
    /**
     * URI, для которого собирается статистика.
     */
    String uri;
    /**
     * Количество хитов (просмотров) по данному URI.
     */
    Long hits;
}