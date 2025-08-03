package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.utils.RequestStatus;

import java.util.List;

/**
 * Репозиторий для работы с сущностями Request (заявки на участие).
 * Содержит методы для поиска, подсчёта и проверки заявок по различным критериям.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Проверяет, существует ли заявка пользователя на участие в событии.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return true, если заявка существует
     */
    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    /**
     * Получить все заявки пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список заявок
     */
    List<Request> findByRequesterId(Long userId);

    /**
     * Подсчитать количество заявок по событию и статусу.
     *
     * @param eventId идентификатор события
     * @param status  статус заявки
     * @return количество заявок
     */
    long countByEventIdAndStatus(Long eventId, RequestStatus status);

    /**
     * Подсчитать количество заявок по списку событий и статусу.
     *
     * @param eventIds список идентификаторов событий
     * @param status   статус заявки
     * @return список массивов: [id события, количество заявок]
     */
    @Query("SELECT r.event.id, COUNT(r) FROM Request r " +
            "WHERE r.event.id IN :eventIds AND r.status = :status " +
            "GROUP BY r.event.id")
    List<Object[]> countByEventIdInAndStatus(@Param("eventIds") List<Long> eventIds, @Param("status") RequestStatus status);

    /**
     * Получить все заявки на участие в событии.
     *
     * @param event событие
     * @return список заявок
     */
    List<Request> findByEvent(Event event);
}