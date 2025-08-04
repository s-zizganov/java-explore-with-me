package ru.practicum.stat.mapper;

import org.mapstruct.Mapper;
import ru.practicum.stat.HitDto;
import ru.practicum.stat.model.Hit;

/**
 * Маппер для преобразования между сущностью Hit и DTO HitDto.
 */
@Mapper(componentModel = "spring")
public interface HitMapper {

    /**
     * Преобразует сущность Hit в DTO HitDto.
     * @param hit сущность Hit
     * @return DTO HitDto
     */
    HitDto hitToHitDto(Hit hit);

    /**
     * Преобразует DTO HitDto в сущность Hit.
     * @param hitDto DTO HitDto
     * @return сущность Hit
     */
    Hit hitDtoToHit(HitDto hitDto);

}
