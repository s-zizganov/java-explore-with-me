package ru.practicum.ewm.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.categories.dto.CategoryMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.location.dto.LocationMapper;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.utils.EventState;

/**
 * Маппер для преобразования между сущностями Event и их DTO.
 * Использует MapStruct для автоматического маппинга.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, UserMapper.class, CategoryMapper.class})
public interface EventMapper {

    /**
     * Преобразует Event в EventShortDto (краткая информация о событии).
     *
     * @param event сущность события
     * @return краткое DTO события
     */
    EventShortDto toShortDto(Event event);

    /**
     * Преобразует EventCreateDto и состояние в сущность Event.
     * Игнорирует поля id, initiator, createdOn, publishedOn, confirmedRequests, views, category.
     *
     * @param eventCreateDto DTO для создания события
     * @param state          состояние события
     * @return сущность Event
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "category", ignore = true)
    Event toEvent(EventCreateDto eventCreateDto, EventState state);

    /**
     * Преобразует Event в EventFullDto (полная информация о событии).
     *
     * @param event сущность события
     * @return полное DTO события
     */
    EventFullDto toFullDto(Event event);
}
