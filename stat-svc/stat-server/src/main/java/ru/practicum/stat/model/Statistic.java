package ru.practicum.stat.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Модель для представления агрегированной статистики по посещениям.
 * Используется для хранения количества хитов (просмотров) по каждому приложению и URI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Statistic {
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
