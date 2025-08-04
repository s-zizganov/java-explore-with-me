package ru.practicum.stat.mapper;

import org.mapstruct.Mapper;
import ru.practicum.stat.ViewStatsDto;
import ru.practicum.stat.model.Statistic;

/**
 * Маппер для преобразования между сущностью Statistic и DTO ViewStatsDto.
 */
@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    /**
     * Преобразует сущность Statistic в DTO ViewStatsDto.
     * @param statistic сущность Statistic
     * @return DTO ViewStatsDto
     */
    ViewStatsDto toStatisticDto(Statistic statistic);

    /**
     * Преобразует DTO ViewStatsDto в сущность Statistic.
     * @param viewStatsDto DTO ViewStatsDto
     * @return сущность Statistic
     */
    Statistic toStatistic(ViewStatsDto viewStatsDto);
}
