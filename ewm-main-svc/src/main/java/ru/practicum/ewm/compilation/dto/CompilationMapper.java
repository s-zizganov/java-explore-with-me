package ru.practicum.ewm.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Set;

/**
 * Маппер для преобразования между сущностью Compilation и её DTO.
 */
@Mapper(componentModel = "spring")
public interface CompilationMapper {
    /**
     * Преобразует DTO создания подборки и множество событий в сущность Compilation.
     * @param compilationCreateDto DTO для создания подборки
     * @param events множество событий, входящих в подборку
     * @return сущность Compilation
     */
    @Mapping(target = "events", source = "events")
    Compilation toCompilationWithEvents(CompilationCreateDto compilationCreateDto, Set<Event> events);

    /**
     * Преобразует сущность Compilation и список кратких DTO событий в DTO подборки.
     * @param compilation сущность подборки
     * @param eventShortDtoList список кратких DTO событий
     * @return DTO подборки событий
     */
    CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtoList);
}
