package ru.practicum.ewm.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.ewm.MainApp;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Тестирование CategoryServiceImp")
public class CategoryServiceImpTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventRepository eventRepository;

    private static final String CATEGORY_NAME_1 = "Фильмы";
    private static final String CATEGORY_NAME_2 = "Книги";
    private static final String UPDATED_CATEGORY_NAME = "Сериалы";

    @Test
    @DisplayName("Создание категории с уникальным именем")
    void createCategory_UniqueName_ReturnCategory() {
        CategoryDto dto = new CategoryDto(CATEGORY_NAME_1);

        Category created = categoryService.create(dto);

        assertNotNull(created.getId());
        assertEquals(dto.getName(), created.getName());
    }

    @Test
    @DisplayName("Попытка создать категорию с неуникальным именем")
    void createCategory_DuplicateName_ThrowConflictException() {
        CategoryDto dto = new CategoryDto(CATEGORY_NAME_1);
        categoryService.create(dto);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> categoryService.create(dto),
                "Ожидается, что будет выброшено исключение ConflictException"
        );

        assertTrue(exception.getMessage().contains("Категория с именем: " + dto.getName() + " уже существует"));
    }

    @Test
    @DisplayName("Удаление существующей категории без связей")
    void deleteCategory_ExistingIdWithoutEvents_DeletedSuccessfully() {
        Category created = categoryService.create(new CategoryDto(CATEGORY_NAME_1));
        Long id = created.getId();

        assertDoesNotThrow(() -> categoryService.delete(id));
        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(id));
    }

    @Test
    @DisplayName("Попытка удаления категории, связанной с событиями")
    void deleteCategory_WithLinkedEvents_ThrowConflictException() {
        Category created = categoryService.create(new CategoryDto(CATEGORY_NAME_1));
        Long id = created.getId();

        Event event = new Event();
        event.setCategory(new Category(id, CATEGORY_NAME_1));
        eventRepository.save(event);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> categoryService.delete(id),
                "Ожидается, что будет выброшено исключение ConflictException"
        );

        assertTrue(exception.getMessage().contains("Невозможно удалить категорию, так как с ней связаны события."));
    }

    @Test
    @DisplayName("Попытка удалить несуществующую категорию")
    void deleteCategory_NonExistingId_ThrowNotFoundException() {
        Long nonExistentId = 999L;

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> categoryService.delete(nonExistentId),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );

        assertTrue(exception.getMessage().contains("Категории с id " + nonExistentId + " не существует"));
    }

    @Test
    @DisplayName("Обновление имени категории на уникальное")
    void updateCategory_ChangeNameToUnique_SuccessfulUpdate() {
        Category created = categoryService.create(new CategoryDto(CATEGORY_NAME_1));
        Long id = created.getId();
        CategoryDto dto = new CategoryDto(UPDATED_CATEGORY_NAME);

        Category updated = categoryService.update(id, dto);

        assertNotNull(updated);
        assertEquals(UPDATED_CATEGORY_NAME, updated.getName());
    }

    @Test
    @DisplayName("Попытка обновить имя категории на занятое другим")
    void updateCategory_ChangeNameToExisting_ThrowConflictException() {
        categoryService.create(new CategoryDto(CATEGORY_NAME_1));
        Category created2 = categoryService.create(new CategoryDto(CATEGORY_NAME_2));
        Long id = created2.getId();
        CategoryDto dto = new CategoryDto(CATEGORY_NAME_1);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> categoryService.update(id, dto),
                "Ожидается, что будет выброшено исключение ConflictException"
        );

        assertTrue(exception.getMessage().contains("Название категории уже занято"));
    }

    @Test
    @DisplayName("Попытка обновить несуществующую категорию")
    void updateCategory_NonExistingId_ThrowNotFoundException() {
        Long nonExistentId = 999L;
        CategoryDto dto = new CategoryDto(UPDATED_CATEGORY_NAME);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> categoryService.update(nonExistentId, dto),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );

        assertTrue(exception.getMessage().contains("Категория с id " + nonExistentId + " не найдена"));
    }

    @Test
    @DisplayName("Получение категории по id")
    void getCategoryById_ExistingId_ReturnCategory() {
        Category created = categoryService.create(new CategoryDto(CATEGORY_NAME_1));
        Long id = created.getId();

        Category found = categoryService.getCategoryById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals(CATEGORY_NAME_1, found.getName());
    }

    @Test
    @DisplayName("Попытка получить несуществующую категорию по id")
    void getCategoryById_NonExistingId_ThrowNotFoundException() {
        Long nonExistentId = 999L;

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> categoryService.getCategoryById(nonExistentId),
                "Ожидается, что будет выброшено исключение NotFoundException"
        );

        assertTrue(exception.getMessage().contains("Категория с id  " + nonExistentId + " не найдена!"));
    }

    @Test
    @DisplayName("Получение списка категорий с пагинацией")
    void getAllCategories_WithPagination_ReturnListOfCategories() {
        categoryService.create(new CategoryDto(CATEGORY_NAME_1));
        categoryService.create(new CategoryDto(CATEGORY_NAME_2));

        List<Category> categories = categoryService.getAllCategories(0, 10);

        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertEquals(2, categories.size());
    }
}
