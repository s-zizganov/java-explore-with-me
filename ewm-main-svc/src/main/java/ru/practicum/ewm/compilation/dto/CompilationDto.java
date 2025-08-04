package ru.practicum.ewm.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

/**
 * DTO для передачи информации о подборке событий.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    /**
     * Идентификатор подборки.
     */
    Long id;
    /**
     * Список кратких DTO событий, входящих в подборку.
     */
    List<EventShortDto> events;
    /**
     * Признак закрепления подборки на главной странице.
     */
    Boolean pinned;
    /**
     * Заголовок подборки.
     */
    String title;
}