package ru.practicum.ewm.event.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для передачи параметров поиска и фильтрации событий.
 * Используется для публичного и приватного поиска событий с различными критериями.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventSearchDto {
    /**
     * Текст для поиска по аннотации и описанию события.
     */
    String text;
    /**
     * Список идентификаторов категорий для фильтрации.
     */
    List<Long> categories;
    /**
     * Начальная дата и время диапазона поиска.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeStart;
    /**
     * Конечная дата и время диапазона поиска.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeEnd;
    /**
     * Индекс первого элемента для постраничного вывода (offset).
     */
    @PositiveOrZero
    int from = 0;
    /**
     * Размер страницы (количество элементов на странице).
     */
    @Positive
    int size = 10;
    /**
     * Список идентификаторов пользователей для фильтрации.
     */
    List<Long> users;
    /**
     * Список состояний событий для фильтрации.
     */
    List<EventState> states;
    /**
     * Критерий сортировки результатов.
     */
    String sort;
    /**
     * Фильтр по платности события.
     */
    Boolean paid;
    /**
     * Фильтр только по доступным для участия событиям.
     */
    Boolean onlyAvailable;
}
