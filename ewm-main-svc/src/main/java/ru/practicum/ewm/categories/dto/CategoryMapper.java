package ru.practicum.ewm.categories.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.categories.model.Category;

/**
 * Маппер для преобразования между сущностью Category и DTO CategoryDto.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Преобразует DTO CategoryDto в сущность Category.
     * @param categoryDto DTO категории
     * @return сущность Category
     */
    @Mapping(target = "id", ignore = true)
    Category toCategory(CategoryDto categoryDto);

    /**
     * Преобразует сущность Category в DTO CategoryDto.
     * @param category сущность категории
     * @return DTO CategoryDto
     */
    CategoryDto toCategoryDto(Category category);
}
