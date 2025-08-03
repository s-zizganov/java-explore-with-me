package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

/**
 * Репозиторий для работы с подборками событий в базе данных.
 */
@Repository
public interface CompilationRepository extends JpaRepository<Compilation,Long> {

    /**
     * Находит все подборки по признаку pinned с поддержкой пагинации.
     * @param pinned фильтр по закрепленным подборкам
     * @param pageable параметры пагинации
     * @return список подборок событий
     */
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}