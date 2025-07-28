package ru.practicum.stat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.stat.mapper.HitMapper;
import ru.practicum.stat.model.Hit;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование HitMapper")
public class HitMapperTest {

    @Autowired
    HitMapper hitMapper;

    @DisplayName("Преобразовать корректный Hit в HitDto")
    @Test
    void hitToHitDto_allHitFieldsFilled_returnCorrectHitDto() {
        Hit hit = new Hit(1L, "app", "some uri", "123.123.0.0", LocalDateTime.now());
        HitDto hitDto = hitMapper.hitToHitDto(hit);
        assertEquals(hitDto.getId(), hit.getId());
        assertEquals(hitDto.getIp(), hit.getIp());
        assertEquals(hitDto.getApp(), hit.getApp());
        assertEquals(hitDto.getUri(), hit.getUri());
        assertEquals(hitDto.getTimestamp(), hit.getTimestamp());
    }

    @DisplayName("Преобразовать null в HitDto")
    @Test
    void hitToHitDto_withNullHit_returnNullHitDto() {
        HitDto hitDto = hitMapper.hitToHitDto(null);
        assertNull(hitDto);
    }

    @DisplayName("Преобразовать корректный createDtoToHit в Hit")
    @Test
    void createDtoToHit_allFieldsFilled_returnCorrectHit() {
        HitCreateDto hitCreateDto = new HitCreateDto("app", "some uri", "123.123.0.0", LocalDateTime.now());
        Hit hit = hitMapper.createDtoToHit(hitCreateDto);
        assertEquals(hitCreateDto.getIp(), hit.getIp());
        assertEquals(hitCreateDto.getApp(), hit.getApp());
        assertEquals(hitCreateDto.getUri(), hit.getUri());
        assertEquals(hitCreateDto.getTimestamp(), hit.getTimestamp());
    }

    @DisplayName("Преобразовать null в Hit")
    @Test
    void createDtoToHit_withNull_returnNullHit() {
        Hit hit = hitMapper.createDtoToHit(null);
        assertNull(hit);
    }
}
