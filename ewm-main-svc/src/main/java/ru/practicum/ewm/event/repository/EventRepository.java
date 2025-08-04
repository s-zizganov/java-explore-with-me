package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.utils.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями Event.
 * Содержит методы для поиска, проверки существования и получения событий по различным критериям.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Поиск событий для администратора с фильтрацией по пользователям, состояниям, категориям и диапазону дат.
     *
     * @param users       список id пользователей-инициаторов
     * @param states      список состояний событий
     * @param categories  список id категорий
     * @param rangeStart  начало диапазона дат
     * @param rangeEnd    конец диапазона дат
     * @param pageable    параметры пагинации
     * @return список событий, удовлетворяющих условиям
     */
    @Query("""
                SELECT e
                FROM Event AS e
                WHERE (?1 IS NULL or e.initiator.id IN ?1)
                    AND (?2 IS NULL or e.state IN ?2)
                    AND (?3 IS NULL or e.category.id in ?3)
                    AND (CAST(?4 AS timestamp) IS NULL or e.eventDate >= ?4)
                    AND (CAST(?5 AS timestamp) IS NULL or e.eventDate < ?5)
            """)
    List<Event> findAllByAdmin(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable
    );

    /**
     * Поиск всех событий по id инициатора с пагинацией.
     *
     * @param initiatorId id пользователя-инициатора
     * @param pageable    параметры пагинации
     * @return список событий
     */
    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    /**
     * Поиск всех событий по списку id.
     *
     * @param eventIds список id событий
     * @return список событий
     */
    List<Event> findAllByIdIn(List<Long> eventIds);

    /**
     * Проверка существования событий по id категории.
     *
     * @param id id категории
     * @return true, если есть события с данной категорией
     */
    boolean existsByCategoryId(Long id);

    /**
     * Поиск события по id и id инициатора.
     *
     * @param eventId id события
     * @param userId  id пользователя-инициатора
     * @return Optional с найденным событием или пустой
     */
    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    /**
     * Поиск всех событий по спецификации с пагинацией.
     *
     * @param spec     спецификация фильтрации
     * @param pageable параметры пагинации
     * @return страница событий
     */
    Page<Event> findAll(Specification<Event> spec, Pageable pageable);
}