package ru.practicum.ewm.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.request.model.Request;

/**
 * Маппер для преобразования между сущностью Request и её DTO.
 * Использует MapStruct для автоматического маппинга.
 */
@Mapper(componentModel = "spring")
public interface RequestMapper {

    /**
     * Преобразует сущность Request в ParticipationRequestDto.
     * Маппит event.id и requester.id в соответствующие поля DTO.
     *
     * @param request сущность заявки на участие
     * @return DTO заявки на участие
     */
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toRequestDto(Request request);
}