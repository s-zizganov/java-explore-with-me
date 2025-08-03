package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.CategoryMapper;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервисного интерфейса для управления категориями.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;


    /**
     * {@inheritDoc}
     */
    @Override
    public Category create(CategoryDto newCategoryDto) {
        if (categoryRepository.existsByNameIgnoreCase(newCategoryDto.getName())) {
            throw new ConflictException(String.format("Категория с именем: %s уже существует", newCategoryDto.getName()));
        }
        Category category = categoryMapper.toCategory(newCategoryDto);
        Category createdCategory = categoryRepository.save(category);
        log.info("Создана категория: {}", createdCategory);
        return createdCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        if (eventRepository.existsByCategoryId(id)) {
            throw new ConflictException("Невозможно удалить категорию, так как с ней связаны события.");
        }
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException(String.format("Категории с id %d не существует", id));
        }
        categoryRepository.deleteById(id);
        log.info("Категория с id: {} успешно удалена", id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category update(Long catId, CategoryDto updateCategoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория с id %d не найдена", catId)));
        String newName = updateCategoryDto.getName().trim().toLowerCase();
        String oldName = category.getName().trim().toLowerCase();

        if (!oldName.equals(newName) && categoryRepository.existsByNameIgnoreCase(newName)) {
            throw new ConflictException("Название категории уже занято");
        }
        category.setName(updateCategoryDto.getName());
        log.info("Имя категории с id {} изменено с '{}' на '{}'", catId, oldName, newName);
        Category updatedCategory = categoryRepository.save(category);
        log.info("Категория с id {} успешно обновлена.", catId);
        return updatedCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id  " + catId + " не найдена!"));
        return category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories(int from, int size) {
        int page = from > 0 ? from / size : 0;
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable)
                .stream().collect(Collectors.toList());
    }
}
