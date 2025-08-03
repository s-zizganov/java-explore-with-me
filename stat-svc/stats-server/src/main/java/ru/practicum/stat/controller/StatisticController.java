package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.HitDto;
import ru.practicum.stat.ViewStatsDto;
import ru.practicum.stat.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
/**
 * REST-контроллер для работы со статистикой посещений.
 * Позволяет сохранять информацию о посещениях и получать агрегированную статистику.
 */
public class StatisticController {
    /**
     * Константа для формата даты и времени.
     */
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Сервис для обработки логики статистики.
     */
    private final StatisticService statisticsService;

    /**
     * Эндпоинт для сохранения информации о посещении (hit).
     * @param hit DTO с данными о посещении
     * @return созданный объект посещения
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public HitDto create(@RequestBody HitDto hit) {
        log.info("Запрос на создание нового Hit: {}", hit);
        return statisticsService.createHit(hit);
    }

    /**
     * Эндпоинт для получения статистики посещений за указанный период.
     * @param start начало периода (формат: yyyy-MM-dd HH:mm:ss)
     * @param end конец периода (формат: yyyy-MM-dd HH:mm:ss)
     * @param uris список URI для фильтрации (опционально)
     * @param unique учитывать только уникальные IP-адреса
     * @return список агрегированных данных по посещениям
     */
    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос получения статистики");
        return statisticsService.getStats(start, end, uris, unique);
    }
}
