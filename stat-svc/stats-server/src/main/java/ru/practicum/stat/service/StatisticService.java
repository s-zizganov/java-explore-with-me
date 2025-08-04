package ru.practicum.stat.service;

import ru.practicum.stat.HitDto;
import ru.practicum.stat.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервисный интерфейс для работы со статистикой посещений.
 * Определяет методы для сохранения информации о посещениях и получения агрегированной статистики.
 */
public interface StatisticService {
    /**
     * Сохраняет информацию о новом посещении (hit).
     * @param createDto DTO с данными о посещении
     * @return созданный объект посещения
     */
    HitDto createHit(HitDto createDto);

    /**
     * Получает статистику посещений за указанный период с возможностью фильтрации по URI и уникальности IP.
     * @param start начало периода
     * @param end конец периода
     * @param uris список URI для фильтрации (может быть null или пустым)
     * @param unique учитывать только уникальные IP-адреса
     * @return список агрегированных данных по посещениям
     */
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
