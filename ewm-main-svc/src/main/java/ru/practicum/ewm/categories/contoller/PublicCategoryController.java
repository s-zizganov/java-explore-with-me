package ru.practicum.ewm.categories.contoller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;

import java.util.List;

/**
 * Публичный контроллер для получения информации о категориях.
 * Позволяет получать отдельную категорию по id и список всех категорий с пагинацией.
 */
@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService service;

    /**
     * Получает категорию по её идентификатору.
     * @param catId идентификатор категории
     * @return найденная категория
     */
    @GetMapping("/{catId}")
    public Category getCategoryById(@PathVariable Long catId) {
        log.info("Запрос на получение категории c id: {}", catId);
        return service.getCategoryById(catId);
    }

    /**
     * Получает список всех категорий с поддержкой пагинации.
     * @param from индекс первого элемента (нумерация с 0)
     * @param size количество элементов для вывода
     * @return список категорий
     */
    @GetMapping
    public List<Category> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение списка всех категорий с параметрами:" +
                        "\n from: {}" +
                        "\n size: {}",
                from, size);
        return service.getAllCategories(from, size);
    }
}
