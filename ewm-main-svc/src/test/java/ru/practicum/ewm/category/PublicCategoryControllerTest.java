package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.categories.contoller.PublicCategoryController;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicCategoryController.class)
@DisplayName("Тестирование PublicCategoryController")
public class PublicCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @DisplayName("Получение категории по ID")
    @Test
    void getCategoryById_validId_shouldReturnCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Books");

        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @DisplayName("Попытка получить несуществующую категорию")
    @Test
    void getCategoryById_nonExistingId_shouldReturnNotFound() throws Exception {
        doThrow(new NotFoundException("Категория с id=999 не найдена"))
                .when(categoryService).getCategoryById(999L);

        mockMvc.perform(get("/categories/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Получение всех категорий с пагинацией")
    @Test
    void getAllCategories_withValidPagination_shouldReturnListOfCategories() throws Exception {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Books");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Electronics");

        List<Category> categories = List.of(category1, category2);

        when(categoryService.getAllCategories(0, 10)).thenReturn(categories);

        mockMvc.perform(get("/categories")
                        .param("from", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Books"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Electronics"));
    }

    @DisplayName("Запрос без параметров пагинации должен использовать значения по умолчанию")
    @Test
    void getAllCategories_withoutParams_shouldUseDefaultPagination() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Books");

        when(categoryService.getAllCategories(0, 10)).thenReturn(List.of(category));

        mockMvc.perform(get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Books"));
    }

    @DisplayName("Параметр from отрицательный — должен вернуть ошибку")
    @Test
    void getAllCategories_negativeFrom_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/categories")
                        .param("from", "-5")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Параметр size неположительный — должен вернуть ошибку")
    @Test
    void getAllCategories_nonPositiveSize_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/categories")
                        .param("from", "0")
                        .param("size", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}