package ru.practicum.stat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.stat.mapper.StatisticMapper;
import ru.practicum.stat.model.Statistic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование StatisticMapper")
public class StatisticMapperTest {

    @Autowired
    StatisticMapper statisticMapper;

    @DisplayName("Преобразовать корректный Statistic в StatisticDto")
    @Test
    void toStatisticDto_allStatisticFieldsFilled_returnStatisticDto() {
        Statistic statistic = new Statistic("app", "some uri", 5L);
        StatisticDto statisticDto = statisticMapper.toStatisticDto(statistic);
        assertEquals(statisticDto.getApp(), statistic.getApp());
        assertEquals(statisticDto.getUri(), statistic.getUri());
        assertEquals(statisticDto.getHits(), statistic.getHits());
    }

    @DisplayName("Преобразовать null в StatisticDto")
    @Test
    void toStatisticDto_withNullStatistic_returnNullStatisticDto() {
        StatisticDto statisticDto = statisticMapper.toStatisticDto(null);
        assertNull(statisticDto);

    }

    @DisplayName("Преобразовать корректный StatisticDto в Statistic")
    @Test
    void toStatisticDto_allStatisticDtoFieldsFilled_returnStatistic() {
        StatisticDto statisticDto = new StatisticDto("app", "some uri", 5L);
        Statistic statistic = statisticMapper.toStatistic(statisticDto);
        assertEquals(statisticDto.getApp(), statistic.getApp());
        assertEquals(statisticDto.getUri(), statistic.getUri());
        assertEquals(statisticDto.getHits(), statistic.getHits());
    }

    @DisplayName("Преобразовать null в Statistic")
    @Test
    void toStatistic_withNullStatisticDto_returnNullStatistic() {
        Statistic statistic = statisticMapper.toStatistic(null);
        assertNull(statistic);

    }
}
