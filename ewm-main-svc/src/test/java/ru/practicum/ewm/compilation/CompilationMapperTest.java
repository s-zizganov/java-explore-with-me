package ru.practicum.ewm.compilation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование CompilationMapper")
public class CompilationMapperTest {
    @Autowired
    CompilationMapper compilationMapper;
    @Autowired
    EventMapper eventMapper;

    @DisplayName("Преобразовать корректный CompilationCreateDto в Compilation")
    @Test
    void toCompilation_allCompilationCreateDtoFieldsFilled_returnCorrectCompilation() {
        CompilationCreateDto compilationCreateDto = new CompilationCreateDto(1L, null,
                true, "Title");
        Event event1 = new Event();
        event1.setTitle("new event 1");
        Event event2 = new Event();
        event2.setTitle("new event 2");
        Set<Event> events = new HashSet<>(List.of(event1, event2));
        Compilation compilation = compilationMapper.toCompilationWithEvents(compilationCreateDto, events);
        assertEquals(compilation.getId(), compilationCreateDto.getId());
        assertEquals(compilation.getEvents(), events);
        assertEquals(compilation.getTitle(), compilationCreateDto.getTitle());
        assertEquals(compilation.getPinned(), compilationCreateDto.getPinned());
    }

    @DisplayName("Преобразовать null в Compilation")
    @Test
    void toCompilation_withNullCompilationCreateDto_returnNullCompilation() {
        Compilation compilation = compilationMapper.toCompilationWithEvents(null, null);
        assertNull(compilation);
    }

    @DisplayName("Преобразовать корректный Compilation в CompilationDto")
    @Test
    void toCompilationDto_allCompilationFieldsFilled_returnCorrectCompilationDto() {
        Event event1 = new Event();
        event1.setTitle("new event 1");
        Event event2 = new Event();
        event2.setTitle("new event 2");
        Set<Event> events = new HashSet<>(List.of(event1, event2));
        Compilation compilation = new Compilation(1L, true, "Title", events);
        CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation,
                events.stream().map(eventMapper::toShortDto).toList());
        assertEquals(compilation.getId(), compilationDto.getId());
        assertEquals(compilation.getPinned(), compilationDto.getPinned());
        assertEquals(compilation.getTitle(), compilationDto.getTitle());
        assertEquals(events.stream().map(eventMapper::toShortDto).toList(), compilationDto.getEvents());
    }

    @DisplayName("Преобразовать null в CompilationDto")
    @Test
    void toCompilationDto_withNullCompilation_returnNullCompilationDto() {
        CompilationDto compilationDto = compilationMapper.toCompilationDto(null, null);
        assertNull(compilationDto);
    }
}
