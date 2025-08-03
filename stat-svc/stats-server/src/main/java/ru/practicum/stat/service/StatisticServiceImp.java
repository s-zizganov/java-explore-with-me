package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.HitDto;
import ru.practicum.stat.ViewStatsDto;
import ru.practicum.stat.mapper.HitMapper;
import ru.practicum.stat.mapper.ViewStatsMapper;
import ru.practicum.stat.model.Hit;
import ru.practicum.stat.model.Statistic;
import ru.practicum.stat.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервисного интерфейса для работы со статистикой посещений.
 * Сохраняет информацию о посещениях и предоставляет агрегированные данные по статистике.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatisticServiceImp implements StatisticService {

    /**
     * Репозиторий для работы с посещениями и статистикой.
     */
    private final StatisticRepository statisticRepository;

    /**
     * Маппер для преобразования между DTO и сущностями Hit.
     */
    private final HitMapper hitMapper;

    /**
     * Маппер для преобразования между DTO и сущностями Statistic.
     */
    private final ViewStatsMapper viewStatsMapper;

    /**
     * Сохраняет информацию о новом посещении (hit).
     * @param createDto DTO с данными о посещении
     * @return созданный объект посещения
     */
    @Override
    public HitDto createHit(HitDto createDto) {
        Hit hit = hitMapper.hitDtoToHit(createDto);
        Hit createdHit = statisticRepository.save(hit);
        log.info("Создан Hit с данными: {}", createdHit);
        return hitMapper.hitToHitDto(createdHit);
    }

    /**
     * Получает статистику посещений за указанный период с возможностью фильтрации по URI и уникальности IP.
     * @param start начало периода
     * @param end конец периода
     * @param uris список URI для фильтрации (может быть null или пустым)
     * @param unique учитывать только уникальные IP-адреса
     * @return список агрегированных данных по посещениям
     */
    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Получение статистики с start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Дата начала диапазона должна быть ДО даты конца диапазона");
        }
        List<Statistic> viewStats;

        if (unique) {
            if (uris != null && !uris.isEmpty()) {
                viewStats = statisticRepository.findStatsUniqueIp(start, end, uris);
            } else {
                viewStats = statisticRepository.findStatsUniqueIpAllUris(start, end);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                viewStats = statisticRepository.findStats(start, end, uris);
            } else {
                viewStats = statisticRepository.findStatsAllUris(start, end);
            }
        }
        log.info("Получена статистика: {}", viewStats);

        return viewStats != null ? viewStats.stream()
                .map(viewStatsMapper::toStatisticDto)
                .collect(Collectors.toList()) : List.of();
    }
}
