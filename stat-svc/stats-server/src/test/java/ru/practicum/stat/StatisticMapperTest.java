package ru.practicum.stat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.stat.mapper.ViewStatsMapper;
import ru.practicum.stat.model.Statistic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование StatisticMapper")
public class StatisticMapperTest {

    @Autowired
    ViewStatsMapper viewStatsMapper;

    @DisplayName("Преобразовать корректный Statistic в StatisticDto")
    @Test
    void toStatisticDto_allStatisticFieldsFilled_returnStatisticDto() {
        Statistic statistic = new Statistic("app", "some uri", 5L);
        ViewStatsDto viewStatsDto = viewStatsMapper.toStatisticDto(statistic);
        assertEquals(viewStatsDto.getApp(), statistic.getApp());
        assertEquals(viewStatsDto.getUri(), statistic.getUri());
        assertEquals(viewStatsDto.getHits(), statistic.getHits());
    }

    @DisplayName("Преобразовать null в StatisticDto")
    @Test
    void toStatisticDto_withNullStatistic_returnNullStatisticDto() {
        ViewStatsDto viewStatsDto = viewStatsMapper.toStatisticDto(null);
        assertNull(viewStatsDto);

    }

    @DisplayName("Преобразовать корректный StatisticDto в Statistic")
    @Test
    void toStatisticDto_allStatisticDtoFieldsFilled_returnStatistic() {
        ViewStatsDto viewStatsDto = new ViewStatsDto("app", "some uri", 5L);
        Statistic statistic = viewStatsMapper.toStatistic(viewStatsDto);
        assertEquals(viewStatsDto.getApp(), statistic.getApp());
        assertEquals(viewStatsDto.getUri(), statistic.getUri());
        assertEquals(viewStatsDto.getHits(), statistic.getHits());
    }

    @DisplayName("Преобразовать null в Statistic")
    @Test
    void toStatistic_withNullStatisticDto_returnNullStatistic() {
        Statistic statistic = viewStatsMapper.toStatistic(null);
        assertNull(statistic);

    }
}
