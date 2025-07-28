package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.HitCreateDto;
import ru.practicum.stat.HitDto;
import ru.practicum.stat.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST-контроллер для работы со статистикой посещений.
 * Позволяет сохранять информацию о посещениях и получать агрегированную статистику.
 */
@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class StatisticController {
    /**
     * Сервис для обработки логики статистики.
     */
    private final StatisticService statisticsService;

    /**
     * Эндпоинт для сохранения информации о посещении (hit).
     * @param hit DTO с данными о посещении
     * @return созданный объект посещения
     */
    @PostMapping("/hit")
    public HitDto create(@RequestBody HitCreateDto hit) {
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
    public List<StatisticDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос получения статистики");
        return statisticsService.getStats(start, end, uris, unique);
    }
}
