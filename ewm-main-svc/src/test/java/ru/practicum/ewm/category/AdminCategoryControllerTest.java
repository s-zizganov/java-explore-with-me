package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.categories.controller.AdminCategoryController;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.categories.service.CategoryService;
import ru.practicum.ewm.exception.NotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для проверки функциональности AdminCategoryController.
 */
@WebMvcTest(AdminCategoryController.class)
@DisplayName("Тесты контроллера категорий для администраторов")
public class AdminCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper jsonMapper;

    @MockBean
    private CategoryService catService;

    @MockBean
    private CategoryRepository catRepository;

    @Test
    @DisplayName("Проверка успешного создания категории")
    void testAddCategory_validInput_returnsCreatedStatus() throws Exception {
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Literature");

        Category newCategory = new Category();
        newCategory.setId(1L);
        newCategory.setName("Literature");

        when(catService.create(any(CategoryDto.class))).thenReturn(newCategory);

        mvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Literature"));
    }

    @Test
    @DisplayName("Проверка создания категории с пустым именем")
    void testAddCategory_emptyName_returnsBadRequest() throws Exception {
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("");

        mvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка создания категории с длинным именем")
    void testAddCategory_overLengthName_returnsBadRequest() throws Exception {
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("x".repeat(51));

        mvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка успешного обновления категории")
    void testUpdateCategory_validData_returnsOkStatus() throws Exception {
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Modified Category");

        Category modifiedCategory = new Category();
        modifiedCategory.setId(1L);
        modifiedCategory.setName("Modified Category");

        when(catService.update(eq(1L), any(CategoryDto.class))).thenReturn(modifiedCategory);

        mvc.perform(patch("/admin/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Modified Category"));
    }

    @Test
    @DisplayName("Проверка удаления категории по ID")
    void testRemoveCategory_validId_returnsNoContent() throws Exception {
        doNothing().when(catService).delete(1L);

        mvc.perform(delete("/admin/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Проверка обновления несуществующей категории")
    void testUpdateCategory_invalidId_returnsNotFound() throws Exception {
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Modified Category");

        doThrow(new NotFoundException("Категория с ID 999 не найдена"))
                .when(catService).update(eq(999L), any(CategoryDto.class));

        mvc.perform(patch("/admin/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(inputDto)))
                .andExpect(status().isNotFound());
    }
}