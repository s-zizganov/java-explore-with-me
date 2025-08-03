package ru.practicum.ewm.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.CategoryMapper;
import ru.practicum.ewm.categories.model.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("Тестирование CategoryMapper")
public class CategoryMapperTest {
    @Autowired
    CategoryMapper categoryMapper;

    @DisplayName("Преобразовать корректный CategoryDto в Category")
    @Test
    void toUser_allUserCreateDtoFieldsFilled_returnCorrectUser() {
        CategoryDto categoryDto = new CategoryDto("CategoryName");
        Category category = categoryMapper.toCategory(categoryDto);
        assertEquals(categoryDto.getName(), category.getName());
    }

    @DisplayName("Преобразовать null в Category")
    @Test
    void toUser_withNullUserCreateDto_returnNullUser() {
        Category category = categoryMapper.toCategory(null);
        assertNull(category);
    }

}
