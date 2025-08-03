package ru.practicum.ewm.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.location.model.Location;

/**
 * Репозиторий для работы с сущностями Location.
 * Предоставляет стандартные CRUD операции для работы с местоположениями.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {

}