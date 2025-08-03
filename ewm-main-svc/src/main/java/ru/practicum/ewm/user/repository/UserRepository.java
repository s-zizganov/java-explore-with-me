package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Репозиторий для работы с сущностями User.
 * Содержит методы для поиска пользователей по email и списку id с пагинацией.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email пользователя
     * @return true, если пользователь существует
     */
    boolean existsByEmail(String email);

    /**
     * Получить пользователей по списку id с пагинацией.
     *
     * @param ids      коллекция идентификаторов пользователей
     * @param pageable параметры пагинации
     * @return список пользователей
     */
    List<User> findByIdIn(Collection<Long> ids, Pageable pageable);
}