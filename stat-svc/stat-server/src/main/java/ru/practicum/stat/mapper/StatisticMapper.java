package ru.practicum.stat.mapper;

import org.mapstruct.Mapper;
import ru.practicum.stat.StatisticDto;
import ru.practicum.stat.model.Statistic;

/**
 * Маппер для преобразования между сущностью Statistic и её DTO.
 * Использует MapStruct для автоматической генерации кода преобразования.
 */
@Mapper(componentModel = "spring")
public interface StatisticMapper {

    /**
     * Преобразует сущность Statistic в DTO для передачи данных наружу.
     * @param statistic сущность статистики
     * @return DTO статистики
     */
    StatisticDto toStatisticDto(Statistic statistic);

    /**
     * Преобразует DTO статистики в сущность Statistic.
     * @param statisticDto DTO статистики
     * @return сущность Statistic
     */
    Statistic toStatistic(StatisticDto statisticDto);
}
