package ru.practicum.stat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stat.HitCreateDto;
import ru.practicum.stat.HitDto;
import ru.practicum.stat.model.Hit;

/**
 * Маппер для преобразования между сущностями и DTO, связанными с посещениями (Hit).
 * Использует MapStruct для автоматической генерации кода преобразования.
 */
@Mapper(componentModel = "spring")
public interface HitMapper {

    /**
     * Преобразует DTO создания посещения в сущность Hit.
     * Поле id игнорируется, так как оно генерируется базой данных.
     * @param createDto DTO для создания посещения
     * @return сущность Hit
     */
    @Mapping(target = "id", ignore = true)
    Hit createDtoToHit(HitCreateDto createDto);

    /**
     * Преобразует сущность Hit в DTO для передачи данных наружу.
     * @param hit сущность посещения
     * @return DTO посещения
     */
    HitDto hitToHitDto(Hit hit);

}
