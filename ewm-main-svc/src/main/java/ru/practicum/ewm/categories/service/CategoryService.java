package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;

import java.util.List;

/**
 * Сервисный интерфейс для управления категориями.
 */
public interface CategoryService {

    /**
     * Создает новую категорию.
     * @param newCategoryDto DTO новой категории
     * @return созданная категория
     */
    Category create(CategoryDto newCategoryDto);

    /**
     * Удаляет категорию по идентификатору.
     * @param id идентификатор категории
     */
    void delete(Long id);

    /**
     * Обновляет существующую категорию по идентификатору.
     * @param catId идентификатор категории
     * @param updateCategoryDto DTO с обновленными данными
     * @return обновленная категория
     */
    Category update(Long catId, CategoryDto updateCategoryDto);

    /**
     * Получает категорию по идентификатору.
     * @param catId идентификатор категории
     * @return найденная категория
     */
    Category getCategoryById(Long catId);

    /**
     * Получает список всех категорий с поддержкой пагинации.
     * @param from индекс первого элемента (нумерация с 0)
     * @param size количество элементов для вывода
     * @return список категорий
     */
    List<Category> getAllCategories(int from, int size);
}
