package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.categories.contoller.AdminCategoryController;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoryService;
import ru.practicum.ewm.exception.NotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCategoryController.class)
@DisplayName("Тестирование AdminCategoryController")
public class AdminCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @DisplayName("Успешное создание категории")
    @Test
    void createCategory_correctCategoryDto_shouldReturnCreated() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("Books");

        Category createdCategory = new Category();
        createdCategory.setId(1L);
        createdCategory.setName("Books");

        when(categoryService.create(any(CategoryDto.class))).thenReturn(createdCategory);

        mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @DisplayName("Создание категории с пустым названием должно вернуть ошибку валидации")
    @Test
    void createCategory_emptyName_shouldReturnBadRequest() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("");

        mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Создание категории с длинным названием (>50 символов)")
    @Test
    void createCategory_tooLongName_shouldReturnBadRequest() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("a".repeat(51)); // 51 символ

        mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Успешное обновление категории")
    @Test
    void updateCategory_validData_shouldReturnUpdated() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("Updated Name");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Name");

        when(categoryService.update(eq(1L), any(CategoryDto.class))).thenReturn(updatedCategory);

        mockMvc.perform(patch("/admin/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @DisplayName("Удаление категории по ID")
    @Test
    void deleteCategory_validId_shouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(delete("/admin/categories/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Попытка обновить несуществующую категорию")
    @Test
    void updateCategory_nonExistingId_shouldReturnNotFound() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setName("Updated Name");

        doThrow(new NotFoundException("Категория с id=999 не найдена"))
                .when(categoryService).update(eq(999L), any(CategoryDto.class));

        mockMvc.perform(patch("/admin/categories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}