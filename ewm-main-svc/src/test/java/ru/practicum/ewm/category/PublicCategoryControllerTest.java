package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.categories.controller.PublicCategoryController;
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
@DisplayName("Тестирование контроллера публичных категорий")
public class PublicCategoryControllerTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoryService service;

    @DisplayName("Получение категорий с корректной пагинацией")
    @Test
    void getCategories_validParams_returnsList() throws Exception {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Books");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Electronics");

        List<Category> categories = List.of(category1, category2);

        when(service.getAllCategories(0, 10)).thenReturn(categories);

        client.perform(get("/categories")
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

    @DisplayName("Получение категории по валидному ID")
    @Test
    void getCategory_validId_returnsCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Books");

        when(service.getCategoryById(1L)).thenReturn(category);

        client.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @DisplayName("Запрос категории с несуществующим ID возвращает ошибку")
    @Test
    void getCategory_invalidId_returnsNotFound() throws Exception {
        doThrow(new NotFoundException("Категория с id=999 не найдена"))
                .when(service).getCategoryById(999L);

        client.perform(get("/categories/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Получение категорий с параметрами пагинации по умолчанию")
    @Test
    void getCategories_defaultParams_appliesDefaultPagination() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Books");

        when(service.getAllCategories(0, 10)).thenReturn(List.of(category));

        client.perform(get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Books"));
    }

    @DisplayName("Отрицательный параметр from вызывает ошибку")
    @Test
    void getCategories_negativeFrom_returnsBadRequest() throws Exception {
        client.perform(get("/categories")
                        .param("from", "-5")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Неположительный параметр size вызывает ошибку")
    @Test
    void getCategories_nonPositiveSize_returnsBadRequest() throws Exception {
        client.perform(get("/categories")
                        .param("from", "0")
                        .param("size", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}