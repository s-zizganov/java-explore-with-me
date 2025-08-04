package ru.practicum.ewm.compilation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование CompilationService")
public class CompilationServiceImpTest {

    @Autowired
    CompilationService compilationService;

    @Autowired
    CompilationRepository compilationRepository;

    @Autowired
    EventRepository eventRepository;

    @DisplayName("Создание подборки с валидными данными")
    @Test
    void createCompilation_validData_returnsCompilationDto() {
        Event event1 = new Event(1L, null, null, null, "Event 1",
                "Annotation 1", "Description 1", LocalDateTime.now().plusDays(1),
                true, 100, true, EventState.PENDING, LocalDateTime.now(),
                null, 0, 150);
        Event event2 = new Event(2L, null, null, null, "Event 2",
                "Annotation 2", "Description 2", LocalDateTime.now().plusDays(2),
                false, 50, false, EventState.PUBLISHED, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 0, 300);
        eventRepository.save(event1);
        eventRepository.save(event2);
        Set<Long> eventIds = new HashSet<>(Arrays.asList(1L, 2L));
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setTitle("Тестовая подборка");
        dto.setPinned(false);
        dto.setEvents(eventIds);
        CompilationDto createdDto = compilationService.create(dto);

        assertNotNull(createdDto.getId());
        assertEquals(dto.getTitle(), createdDto.getTitle());
        assertEquals(dto.getPinned(), createdDto.getPinned());
        assertNotNull(createdDto.getEvents());
        assertEquals(eventIds.size(), createdDto.getEvents().size());
    }

    @DisplayName("Создание подборки без событий")
    @Test
    void createCompilation_withoutEvents_returnsCompilationDto() {
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setTitle("Без событий");
        dto.setPinned(true);

        CompilationDto createdDto = compilationService.create(dto);

        assertNotNull(createdDto.getId());
        assertTrue(createdDto.getEvents().isEmpty());
    }

    @DisplayName("Создание подборки без названия должно вызвать ошибку")
    @Test
    void createCompilation_withoutTitle_throwsException() {
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setPinned(false);

        assertThrows(DataIntegrityViolationException.class, () -> compilationService.create(dto));
    }

    @DisplayName("Обновление существующей подборки")
    @Test
    void updateCompilation_validData_returnsUpdatedDto() {
        Event event1 = new Event(1L, null, null, null, "Event 1",
                "Annotation 1", "Description 1", LocalDateTime.now().plusDays(1),
                true, 100, true, EventState.PENDING, LocalDateTime.now(),
                null, 0, 150);
        Event event2 = new Event(2L, null, null, null, "Event 2",
                "Annotation 2", "Description 2", LocalDateTime.now().plusDays(2),
                false, 50, false, EventState.PUBLISHED, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 0, 300);
        eventRepository.save(event1);
        eventRepository.save(event2);
        CompilationCreateDto initialDto = new CompilationCreateDto();
        initialDto.setTitle("Старое название");
        initialDto.setPinned(false);
        initialDto.setEvents(new HashSet<>(Arrays.asList(1L)));
        CompilationDto savedDto = compilationService.create(initialDto);

        CompilationCreateDto updateDto = new CompilationCreateDto();
        updateDto.setTitle("Новое название");
        updateDto.setPinned(true);
        updateDto.setEvents(new HashSet<>(Collections.singletonList(2L)));

        CompilationDto updatedDto = compilationService.update(savedDto.getId(), updateDto);

        assertEquals("Новое название", updatedDto.getTitle());
        assertTrue(updatedDto.getPinned());
        assertEquals(1, updatedDto.getEvents().size());
    }

    @DisplayName("Обновление подборки без указания событий")
    @Test
    void updateCompilation_withoutEvents_doesNotChangeEvents() {
        Event event1 = new Event(1L, null, null, null, "Event 1",
                "Annotation 1", "Description 1", LocalDateTime.now().plusDays(1), true,
                100, true, EventState.PENDING, LocalDateTime.now(),
                null, 0, 150);
        Event event2 = new Event(2L, null, null, null, "Event 2",
                "Annotation 2", "Description 2", LocalDateTime.now().plusDays(2),
                false, 50, false, EventState.PUBLISHED, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 0, 300);
        eventRepository.save(event1);
        eventRepository.save(event2);
        CompilationCreateDto initialDto = new CompilationCreateDto();
        initialDto.setTitle("Старое название");
        initialDto.setPinned(false);
        initialDto.setEvents(new HashSet<>(Arrays.asList(1L, 2L)));
        CompilationDto savedDto = compilationService.create(initialDto);

        CompilationCreateDto updateDto = new CompilationCreateDto();
        updateDto.setTitle("Новое название");

        CompilationDto updatedDto = compilationService.update(savedDto.getId(), updateDto);

        assertEquals(2, updatedDto.getEvents().size());
    }

    @DisplayName("Обновление несуществующей подборки")
    @Test
    void updateCompilation_nonExisting_throwsNotFoundException() {
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setTitle("Несуществующая");

        assertThrows(NotFoundException.class, () -> compilationService.update(999L, dto));
    }

    @DisplayName("Удаление существующей подборки")
    @Test
    void deleteCompilation_existingId_deletesSuccessfully() {
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setTitle("Для удаления");
        CompilationDto createdDto = compilationService.create(dto);

        compilationService.delete(createdDto.getId());

        assertThrows(NotFoundException.class, () -> compilationService.findCompilationById(createdDto.getId()));
    }

    @DisplayName("Удаление несуществующей подборки")
    @Test
    void deleteCompilation_nonExisting_throwsNotFoundException() {
        assertThrows(NotFoundException.class, () -> compilationService.delete(999L));
    }

    @DisplayName("Получение всех подборок")
    @Test
    void getAllCompilations_withoutFilter_returnsAll() {
        List<CompilationDto> all = compilationService.getAllCompilations(0, 10, null);
        assertNotNull(all);
        assertTrue(all.size() >= 0);
    }

    @DisplayName("Получение подборок по pinned = true")
    @Test
    void getAllCompilations_pinnedTrue_returnsFiltered() {
        // Создаем тестовые подборки
        CompilationCreateDto dto1 = new CompilationCreateDto();
        dto1.setTitle("Pinned true");
        dto1.setPinned(true);
        compilationService.create(dto1);

        CompilationCreateDto dto2 = new CompilationCreateDto();
        dto2.setTitle("Pinned false");
        dto2.setPinned(false);
        compilationService.create(dto2);

        List<CompilationDto> all = compilationService.getAllCompilations(0, 10, true);

        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertTrue(all.stream().allMatch(CompilationDto::getPinned));
    }

    @DisplayName("Получение подборки по ID")
    @Test
    void findCompilationById_existingId_returnsDto() {
        CompilationCreateDto dto = new CompilationCreateDto();
        dto.setTitle("Для поиска");
        CompilationDto createdDto = compilationService.create(dto);

        CompilationDto foundDto = compilationService.findCompilationById(createdDto.getId());

        assertNotNull(foundDto);
        assertEquals(createdDto.getId(), foundDto.getId());
    }

    @DisplayName("Получение несуществующей подборки по ID")
    @Test
    void findCompilationById_nonExisting_throwsNotFoundException() {
        assertThrows(NotFoundException.class, () -> compilationService.findCompilationById(999L));
    }
}