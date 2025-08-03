package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;

import java.util.List;

/**
 * Сервисный интерфейс для управления подборками событий.
 */
public interface CompilationService {
    /**
     * Создает новую подборку событий.
     * @param newCompilationCreateDto DTO для создания подборки
     * @return созданная подборка событий
     */
    CompilationDto create(CompilationCreateDto newCompilationCreateDto);

    /**
     * Обновляет существующую подборку событий по id.
     * @param compId идентификатор подборки
     * @param updateCompilationRequest DTO с обновленными данными
     * @return обновленная подборка событий
     */
    CompilationDto update(Long compId, CompilationCreateDto updateCompilationRequest);

    /**
     * Удаляет подборку событий по id.
     * @param id идентификатор подборки
     */
    void delete(Long id);

    /**
     * Получает список подборок событий с возможностью фильтрации по признаку pinned и пагинацией.
     * @param from индекс первого элемента (нумерация с 0)
     * @param size количество элементов для вывода
     * @param pinned фильтр по закрепленным подборкам (может быть null)
     * @return список подборок событий
     */
    List<CompilationDto> getAllCompilations(Integer from, Integer size, Boolean pinned);

    /**
     * Получает подборку событий по её идентификатору.
     * @param compId идентификатор подборки
     * @return найденная подборка событий
     */
    CompilationDto findCompilationById(Long compId);
}

