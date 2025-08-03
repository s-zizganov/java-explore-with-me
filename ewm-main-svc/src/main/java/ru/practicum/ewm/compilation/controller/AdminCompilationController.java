package ru.practicum.ewm.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.utils.Marker;

/**
 * Контроллер для администрирования подборок событий (compilations).
 * Позволяет создавать, обновлять и удалять подборки событий.
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    /**
     * Создает новую подборку событий.
     * @param newCompilationCreateDto DTO для создания подборки
     * @return созданная подборка событий
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Validated(Marker.OnCreate.class)
    public CompilationDto create(@Valid @RequestBody CompilationCreateDto newCompilationCreateDto) {
        log.info("Запрос на добавление подборки событий - ADMIN: {}", newCompilationCreateDto);
        CompilationDto compilation = compilationService.create(newCompilationCreateDto);
        log.info("Подборка событий создана: {}", compilation);
        return compilation;
    }

    /**
     * Обновляет существующую подборку событий по id.
     * @param updateCompilation DTO с обновленными данными
     * @param compId идентификатор подборки
     * @return обновленная подборка событий
     */
    @PatchMapping("/{compId}")
    @Validated(Marker.OnUpdate.class)
    public CompilationDto update(@Valid @RequestBody CompilationCreateDto updateCompilation,
                                 @PathVariable Long compId) {
        log.info("Запрос на обновление подборки событий с id {} - ADMIN: {}", compId, updateCompilation);
        CompilationDto compilation = compilationService.update(compId, updateCompilation);
        log.info("Подборка событий с id={} обновлена: {}", compId, compilation);
        return compilation;
    }

    /**
     * Удаляет подборку событий по id.
     * @param id идентификатор подборки
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Запрос на удаление подборки событий с id {} - ADMIN", id);
        compilationService.delete(id); // Удаляем подборку
        log.info("Подборка событий с id={} удалена", id);
    }
}
