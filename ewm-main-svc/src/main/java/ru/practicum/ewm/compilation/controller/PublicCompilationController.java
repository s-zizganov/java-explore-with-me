package ru.practicum.ewm.compilation.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import java.util.List;

/**
 * Публичный контроллер для получения информации о подборках событий.
 * Позволяет получать список подборок с фильтрацией и отдельную подборку по id.
 */
@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationService compilationService;

    /**
     * Получает список подборок событий с возможностью фильтрации по признаку pinned и пагинацией.
     * @param pinned фильтр по закрепленным подборкам (может быть null)
     * @param from индекс первого элемента (нумерация с 0)
     * @param size количество элементов для вывода
     * @return список подборок событий
     */
    @GetMapping
    public List<CompilationDto> getAllCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос на получение подборок событий с параметрами:" +
                "\npinned={}" +
                "\nfrom={}" +
                "\nsize={}", pinned, from, size);
        List<CompilationDto> compilations = compilationService.getAllCompilations(from, size, pinned);
        log.info("Получен список подборок событий: {}", compilations);
        return compilations;
    }

    /**
     * Получает подборку событий по её идентификатору.
     * @param compId идентификатор подборки
     * @return найденная подборка событий
     */
    @GetMapping("/{compId}")
    public CompilationDto findCompilationById(@PathVariable Long compId) {
        log.info("Запрос на получение подборки событий по id {}", compId);
        CompilationDto compilation = compilationService.findCompilationById(compId);
        log.info("Получена подборка событий по id {}: {}", compId, compilation);
        return compilation;
    }
}
