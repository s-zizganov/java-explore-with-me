package ru.practicum.stat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.stat.service.StatisticService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = StatisticApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование StatisticServiceImpl")
public class StatisticServiceImplTest {

    @Autowired
    StatisticService statisticService;

    @BeforeEach
    void setUp() {
        HitCreateDto hitCreateDto1 = new HitCreateDto("app1", "some uri1", "123.123.0.0", LocalDateTime.now().minusDays(1));
        HitCreateDto hitCreateDto2 = new HitCreateDto("app", "some uri2", "123.124.0.0", LocalDateTime.now().minusHours(2));
        HitCreateDto hitCreateDto3 = new HitCreateDto("app", "some uri3", "123.125.0.0", LocalDateTime.now());
        HitCreateDto hitCreateDto4 = new HitCreateDto("app", "some uri3", "123.126.0.0", LocalDateTime.now());
        HitCreateDto hitCreateDto5 = new HitCreateDto("app", "some uri3", "123.125.0.0", LocalDateTime.now());
        statisticService.createHit(hitCreateDto1);
        statisticService.createHit(hitCreateDto2);
        statisticService.createHit(hitCreateDto3);
        statisticService.createHit(hitCreateDto4);
        statisticService.createHit(hitCreateDto5);

    }

    @Test
    @DisplayName("Создать Hit")
    void createHit_withCorrectHit_returnCorrectHitDto() {
        HitCreateDto hitCreateDto = new HitCreateDto("app", "some uri", "123.123.0.0", LocalDateTime.now());
        HitDto hitDto = statisticService.createHit(hitCreateDto);

        assertEquals(hitDto.getId(), 6L);
        assertEquals(hitCreateDto.getIp(), hitDto.getIp());
        assertEquals(hitCreateDto.getApp(), hitDto.getApp());
        assertEquals(hitCreateDto.getUri(), hitDto.getUri());
        assertEquals(hitCreateDto.getTimestamp(), hitDto.getTimestamp());
    }

    @Test
    @DisplayName("Получить Statistic unique = true, uris не пуст")
    void getStats_uniqueTrueAndUrisNotEmpty_returnStatisticList() {
        List<String> uris = new ArrayList<>(List.of("some uri", "some uri3"));

        List<StatisticDto> statisticDtoList = statisticService.getStats(LocalDateTime.now().minusDays(3), LocalDateTime.now(), uris, true);

        assertEquals(statisticDtoList.size(), 1);
        assertEquals(statisticDtoList.get(0).getHits(), 2);
    }

    @Test
    @DisplayName("Получить Statistic unique = true, uris пуст")
    void getStats_uniqueTrueAndUrisEmpty_returnStatisticList() {
        List<StatisticDto> statisticDtoList = statisticService.getStats(LocalDateTime.now().minusDays(3), LocalDateTime.now(), new ArrayList<>(), true);

        assertEquals(statisticDtoList.size(), 3);
        assertEquals(statisticDtoList.get(0).getHits(), 2);
    }

    @Test
    @DisplayName("Получить Statistic unique = false, uris не пуст")
    void getStats_uniqueFalseAndUrisNotEmpty_returnStatisticList() {
        List<String> uris = new ArrayList<>(List.of("some uri", "some uri3"));

        List<StatisticDto> statisticDtoList = statisticService.getStats(LocalDateTime.now().minusDays(3), LocalDateTime.now(), uris, false);

        assertEquals(statisticDtoList.size(), 1);
        assertEquals(statisticDtoList.get(0).getHits(), 3);
    }

    @Test
    @DisplayName("Получить Statistic unique = false, uris пуст")
    void getStats_uniqueFalseAndUrisEmpty_returnStatisticList() {
        List<StatisticDto> statisticDtoList = statisticService.getStats(LocalDateTime.now().minusDays(3), LocalDateTime.now(), new ArrayList<>(), false);

        assertEquals(statisticDtoList.size(), 3);
        assertEquals(statisticDtoList.get(0).getHits(), 3);
    }

    @Test
    @DisplayName("Получить Statistic unique = false, uris пуст")
    void getStats_StartDataIsAfterEndData_returnIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                statisticService.getStats(LocalDateTime.now(), LocalDateTime.now().minusDays(3), new ArrayList<>(), false)

        );

        assertEquals("Дата начала диапазона должна быть ДО даты конца диапазона", exception.getMessage());
    }

}
