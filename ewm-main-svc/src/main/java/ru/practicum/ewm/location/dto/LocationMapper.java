package ru.practicum.ewm.location.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.location.model.Location;

/**
 * Маппер для преобразования между сущностью Location и её DTO.
 * Использует MapStruct для автоматического маппинга с преобразованием
 * названий полей (lat/lon ↔ latitude/longitude).
 */
@Mapper(componentModel = "spring")
public interface LocationMapper {

    /**
     * Преобразует LocationDto в сущность Location.
     * Игнорирует поле id, маппит lat → latitude, lon → longitude.
     *
     * @param locationDto DTO с данными местоположения
     * @return сущность Location
     */
    @Mapping(target = "longitude", source = "locationDto.lon")
    @Mapping(target = "latitude", source = "locationDto.lat")
    @Mapping(target = "id", ignore = true)
    Location toLocation(LocationDto locationDto);

    /**
     * Преобразует сущность Location в LocationDto.
     * Маппит latitude → lat, longitude → lon.
     *
     * @param location сущность местоположения
     * @return DTO с данными местоположения
     */
    @Mapping(target = "lon", source = "location.longitude")
    @Mapping(target = "lat", source = "location.latitude")
    LocationDto toLocationDto(Location location);
}
