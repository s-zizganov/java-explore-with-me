package ru.practicum.ewm.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.categories.model.Category;

/**
 * Репозиторий для работы с категориями в базе данных.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    /**
     * Проверяет, существует ли категория с указанным названием (без учета регистра).
     * @param name название категории
     * @return true, если категория существует, иначе false
     */
    boolean existsByNameIgnoreCase(String name);
}