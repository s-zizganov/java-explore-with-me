package ru.practicum.stat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представления статистики просмотров по приложению и URI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {
    /**
     * Имя приложения, для которого собирается статистика.
     */
    private String app;
    /**
     * URI, по которому собирается статистика.
     */
    private String uri;
    /**
     * Количество просмотров (хитов) по данному URI.
     */
    private Long hits;
}