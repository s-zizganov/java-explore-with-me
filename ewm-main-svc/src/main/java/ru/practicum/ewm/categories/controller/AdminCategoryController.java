package ru.practicum.ewm.categories.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;

/**
 * Контроллер для админского управления категориями.
 * Поддерживает операции создания, изменения и удаления категорий.
 */
@Slf4j
@RestController
@RequestMapping(AdminCategoryController.BASE_PATH)
@RequiredArgsConstructor
public class AdminCategoryController {

    public static final String BASE_PATH = "/admin/categories";
    public static final String CATEGORY_ID_PATH = "/{categoryId}";


    private final CategoryService categoryService;

    /**
     * Добавляет новую категорию в систему.
     * @param dto данные категории для создания
     * @return созданная категория
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody @Valid CategoryDto dto) {
        log.info("Получен запрос на добавление категории: {}", dto.getName());
        return categoryService.create(dto);
    }

    /**
     * Изменяет данные категории по её идентификатору.
     * @param categoryId идентификатор категории
     * @param dto данные для обновления категории
     * @return обновлённая категория
     */
    @PatchMapping(CATEGORY_ID_PATH)
    public Category modifyCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryDto dto) {
        log.info("Обновление категории с ID: {}", categoryId);
        return categoryService.update(categoryId, dto);
    }

    /**
     * Удаляет категорию по её идентификатору.
     * @param categoryId идентификатор категории для удаления
     */
    @DeleteMapping(CATEGORY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Long categoryId) {
        log.info("Удаление категории с ID: {}", categoryId);
        categoryService.delete(categoryId);
    }
}