package ru.practicum.ewm.categories.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;

/**
 * Контроллер для управления категориями (админский доступ).
 * Позволяет создавать, обновлять и удалять категории.
 */
@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService service;

    /**
     * Создает новую категорию.
     * @param categoryDto DTO категории
     * @return созданная категория
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Category create(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Запрос на создание категории: {}", categoryDto);
        return service.create(categoryDto);
    }

    /**
     * Обновляет существующую категорию по id.
     * @param catId идентификатор категории
     * @param categoryDto DTO с обновленными данными
     * @return обновленная категория
     */
    @PatchMapping("/{catId}")
    public Category update(@PathVariable Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Запрос на обновление категории c id: {}", catId);
        return service.update(catId, categoryDto);
    }

    /**
     * Удаляет категорию по id.
     * @param catId идентификатор категории
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        log.info("Запрос на удаление категории с id: {}", catId);
        service.delete(catId);
    }
}