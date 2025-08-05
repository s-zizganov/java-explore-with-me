package ru.practicum.ewm.categories.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;

import java.util.List;

/**
 * Контроллер для публичных запросов к категориям.
 * Обеспечивает получение категории по ID и списка категорий с пагинацией.
 */
@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {

    private final CategoryService service;

    /**
     * Получает категорию по её идентификатору.
     * @param id идентификатор категории
     * @return объект категории
     */
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        log.info("Запрос категории с ID: {}", id);
        return service.getCategoryById(id);
    }

    /**
     * Получает список категорий с пагинацией.
     * @param from индекс начального элемента (начинается с 0)
     * @param size количество элементов для вывода
     * @return список категорий
     */
    @GetMapping
    public List<Category> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос списка категорий: from={}, size={}", from, size);
        return service.getAllCategories(from, size);
    }
}